# AWS ECS Fargate Deployment

This document defines the deployment contract for MS-SIMULATION after the Spring Modulith migration.

MS-SIMULATION is now deployed as one Spring Boot application:

- One Maven project.
- One Spring Boot main class: `com.gft.simulation.SimulationApplication`.
- One executable JAR.
- One Docker image.
- One ECS task definition.
- One ECS service.

The application still contains two Modulith modules:

- `time`: simulation clock, `POST /tick/{days}`, `GET /tick/current`, and `time.advanced.v1` publication.
- `map`: map read model, `GET /map`, and RabbitMQ listeners for external map events.

The modules remain separated in code, but they no longer run as separate deployment units.

## Deployment Target

The deployment target uses containers and managed AWS runtime services:

- GitHub Actions builds the MS-SIMULATION JAR and Docker image.
- Amazon ECR stores the single Docker image.
- Amazon ECS Fargate runs one `ms-simulation` service.
- Amazon RDS PostgreSQL provides persistence.
- CloudAMQP provides RabbitMQ.
- IAM with GitHub OIDC grants GitHub Actions access to AWS without long-lived AWS keys.
- CloudWatch Logs stores container logs.

This replaces both older deployment models:

- The original EC2 + copied JAR + systemd approach.
- The previous ECS model with separate `ms-time` and `ms-map` images, task definitions and services.

## Runtime Layout

Only one runtime unit should be deployed.

| Runtime | ECR image | ECS service | Container port |
| --- | --- | --- | --- |
| `ms-simulation` | `<aws-account-id>.dkr.ecr.<region>.amazonaws.com/ms-simulation-2026-atmy` | `ms-simulation-service-2026-atmy` | `8080` |

The service exposes both functional areas:

| Area | Endpoint or behavior |
| --- | --- |
| Simulation clock | `GET /tick/current` |
| Simulation clock | `POST /tick/{days}` |
| Map state | `GET /map` |
| RabbitMQ publisher | `time.advanced.v1` |
| RabbitMQ listeners | `truck.registered.v1`, `truck.position.updated.v1`, `truck.deleted.v1`, `warehouse.registered.v1` |

Recommended initial ECS desired count: `1`.

Reason: the map read model keeps an in-memory `MapStateHolder`. Running several replicas without redesigning that state can make RabbitMQ listeners distribute events across instances and leave each instance with a different in-memory map. Scale-out should be treated as a separate architecture task.

## Infrastructure

Expected AWS and external components:

- One ECR repository for `ms-simulation`.
- One ECS cluster using Fargate.
- One ECS task definition for `ms-simulation`.
- One ECS service for `ms-simulation`.
- One CloudWatch log group for `ms-simulation`.
- RDS PostgreSQL database.
- CloudAMQP RabbitMQ broker.
- Security group for ECS tasks.
- Security group for RDS.
- IAM role assumable from GitHub Actions through OIDC.

Optional components:

- Application Load Balancer if HTTP endpoints must be reachable from outside ECS.
- AWS Secrets Manager or SSM Parameter Store for sensitive runtime values.

The old `ms-time` and `ms-map` ECS resources may remain temporarily during migration for rollback, but they are not part of the target deployment model.

## Configuration

Production values must come from environment variables or AWS-managed secrets injected into the ECS task definition.

Expected runtime variables:

```text
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

SPRING_DATASOURCE_URL=jdbc:postgresql://<rds-endpoint>:5432/<database-name>
SPRING_DATASOURCE_USERNAME=<database-user>
SPRING_DATASOURCE_PASSWORD=<database-password>

RABBITMQ_HOST=<cloudamqp-host>
RABBITMQ_PORT=5671
RABBITMQ_USERNAME=<cloudamqp-user>
RABBITMQ_PASSWORD=<cloudamqp-password>
RABBITMQ_VHOST=<cloudamqp-vhost>
RABBITMQ_SSL_ENABLED=true
```

Do not commit real database credentials, RabbitMQ credentials, AWS credentials, CloudAMQP URLs or private keys.

## GitHub Actions

GitHub Actions runs manually with `workflow_dispatch`.

The CI/CD flow is split into two workflows:

1. `Build and Push Docker Image`.
2. `Deploy to Amazon ECS`.

This keeps image publication separate from runtime deployment. A commit can be built and pushed to ECR without immediately changing the running ECS service.

### Build And Push Docker Image

The build workflow should:

1. Check out the repository.
2. Set up Java 21.
3. Cache Maven dependencies if useful.
4. Run the Maven test suite.
5. Build the Spring Boot JAR.
6. Authenticate to AWS through GitHub OIDC.
7. Log in to Amazon ECR.
8. Build the single Docker image from the repository root.
9. Push the image to ECR using the commit SHA as the Docker tag.
10. Print the `image_tag` that must be used by the deploy workflow.

Example image tag:

```text
822414985516.dkr.ecr.eu-west-1.amazonaws.com/ms-simulation-2026-atmy:<commit-sha>
```

Do not use `latest` as the deployment contract. Tags should identify a specific build.

### Deploy To Amazon ECS

The deploy workflow should:

1. Receive an explicit `image_tag` input.
2. Authenticate to AWS through GitHub OIDC.
3. Build the ECR image URI for `ms-simulation` using that tag.
4. Render the ECS task definition with the new image URI.
5. Deploy the `ms-simulation` ECS service.
6. Wait for service stability.

The `image_tag` should normally be the commit SHA printed by `Build and Push Docker Image`.

Prefer GitHub OIDC over long-lived `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` secrets.

Expected GitHub repository variables:

```text
AWS_REGION
AWS_ROLE_TO_ASSUME
ECR_REPOSITORY_MS_SIMULATION=ms-simulation-2026-atmy
ECS_CLUSTER=junior-workshop-2026-vlc
ECS_SERVICE_MS_SIMULATION=ms-simulation-service-2026-atmy
ECS_TASK_DEFINITION_MS_SIMULATION
```

Sensitive values should be GitHub secrets or stored in AWS Secrets Manager/SSM Parameter Store and referenced by ECS.

## ECS Task Definition

The target task definition should contain one container:

```text
family: ms-simulation-2026-atmy
container name: ms-simulation
container port: 8080
image: <aws-account-id>.dkr.ecr.<region>.amazonaws.com/ms-simulation-2026-atmy:<image-tag>
```

The task definition must:

- Use Fargate compatibility.
- Use Java 21 in the Docker image.
- Set `SPRING_PROFILES_ACTIVE=prod`.
- Set `SERVER_PORT=8080`.
- Inject database and RabbitMQ values through environment variables or ECS secrets.
- Configure CloudWatch Logs.
- Use an execution role that can pull from ECR and write logs.
- Use a task role with only the permissions needed by the application.

## Networking And Security

Recommended security model:

- ECS tasks run in private subnets when possible.
- ECS tasks have outbound access to ECR, CloudWatch Logs, RDS and CloudAMQP.
- RDS inbound access is restricted to the ECS task security group.
- Public HTTP access goes through an ALB if required.
- GitHub Actions uses IAM/OIDC and only receives the minimum AWS permissions needed for ECR push and ECS deploy.

If an ALB is used, route both endpoint groups to the same target group:

```text
/tick/* -> ms-simulation
/map    -> ms-simulation
```

## Migration Strategy

Recommended migration path:

1. Keep the existing `ms-time` and `ms-map` services running.
2. Create the new `ms-simulation` ECR repository, task definition, service and log group.
3. Build and push the first `ms-simulation` image.
4. Deploy `ms-simulation` with desired count `1`.
5. Validate HTTP endpoints, database connectivity, RabbitMQ publishing and RabbitMQ consumption.
6. Switch ALB routes or DNS/client configuration to the new service.
7. Stop the old `ms-time` and `ms-map` services after validation.
8. Remove old ECR repositories, task definitions, services and log groups only after rollback is no longer needed.

Rollback during migration is simple while the old services still exist: route traffic back to `ms-time` and `ms-map`, then scale down or stop `ms-simulation`.

## Validation

After deployment, validate at least:

- `ms-simulation` ECS task is running and healthy.
- The container can connect to RDS PostgreSQL.
- The container can connect to CloudAMQP RabbitMQ.
- `GET /tick/current` responds through the chosen access path.
- `POST /tick/{days}` advances the simulation day.
- `time.advanced.v1` is published with the expected payload.
- `GET /map` returns the current map read model.
- Map listeners consume expected external events.
- Container logs are visible in CloudWatch Logs.
