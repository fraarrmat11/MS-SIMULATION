# AWS Manual Setup

This document describes the manual AWS setup for deploying MS-SIMULATION as one Spring Boot Modulith runtime.

The target deployment is:

```text
one JAR -> one Docker image -> one ECS task definition -> one ECS service
```

Runtime name: `ms-simulation`.

The old `ms-time` and `ms-map` ECS resources can remain temporarily during migration, but they are no longer the target architecture.

## 1. Naming

Recommended resource names:

```text
ECR repository:        ms-simulation-2026-atmy
ECS task definition:   ms-simulation-2026-atmy
ECS service:           ms-simulation-service-2026-atmy
Container name:        ms-simulation
CloudWatch log group:  /ecs/ms-simulation-2026-atmy
Container port:        8080
```

Recommended GitHub repository variables:

```text
AWS_REGION=eu-west-1
AWS_ROLE_TO_ASSUME=<github-actions-oidc-role-arn>
ECR_REPOSITORY_MS_SIMULATION=ms-simulation-2026-atmy
ECS_CLUSTER=junior-workshop-2026-vlc
ECS_SERVICE_MS_SIMULATION=ms-simulation-service-2026-atmy
ECS_TASK_DEFINITION_MS_SIMULATION=deployment/ecs/ms-simulation-task-definition.json
```

Use your real AWS account ID, region and role ARN.

## 2. ECR

Create one ECR repository:

```text
ms-simulation-2026-atmy
```

The GitHub Actions build workflow will push images tagged with the commit SHA:

```text
<account-id>.dkr.ecr.<region>.amazonaws.com/ms-simulation-2026-atmy:<commit-sha>
```

Do not rely on `latest` for deployments.

## 3. RDS PostgreSQL

Reuse the existing RDS PostgreSQL database if it already supports the current schema.

The application requires:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

Recommended format:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://<rds-endpoint>:5432/<database-name>
```

Security group rule:

- Allow inbound PostgreSQL traffic on port `5432` only from the ECS task security group.

The application uses Liquibase in production, so the database user must be able to run the configured changelog.

## 4. CloudAMQP

Reuse the existing CloudAMQP instance.

The application requires:

```text
RABBITMQ_HOST
RABBITMQ_PORT=5671
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
RABBITMQ_VHOST
RABBITMQ_SSL_ENABLED=true
```

Do not commit CloudAMQP URLs or credentials.

Prefer storing sensitive values in AWS SSM Parameter Store or AWS Secrets Manager and injecting them into the ECS task definition as secrets.

## 5. Secrets And Parameters

Recommended SSM Parameter Store paths:

```text
/ms-simulation/prod/db/url
/ms-simulation/prod/db/username
/ms-simulation/prod/db/password
/ms-simulation/prod/rabbitmq/host
/ms-simulation/prod/rabbitmq/username
/ms-simulation/prod/rabbitmq/password
/ms-simulation/prod/rabbitmq/vhost
```

Non-sensitive values can be plain ECS environment variables:

```text
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
RABBITMQ_PORT=5671
RABBITMQ_SSL_ENABLED=true
```

## 6. IAM

Use GitHub OIDC. Do not create long-lived AWS access keys for GitHub Actions.

The GitHub Actions role needs permissions for:

- ECR login and image push.
- Reading the target ECS task definition file if stored in the repository.
- Registering a new ECS task definition revision.
- Updating the `ms-simulation` ECS service.
- Passing the ECS execution role and task role.

The ECS task execution role needs:

- Pull image from ECR.
- Write logs to CloudWatch.
- Read ECS-injected secrets if using SSM or Secrets Manager.

The ECS task role should stay minimal. The application currently does not need broad AWS API access at runtime.

## 7. CloudWatch Logs

Create one log group:

```text
/ecs/ms-simulation-2026-atmy
```

The task definition should use:

```text
awslogs-region=<region>
awslogs-group=/ecs/ms-simulation-2026-atmy
awslogs-stream-prefix=ecs
```

## 8. ECS Task Definition

Create one task definition for Fargate:

```text
family: ms-simulation-2026-atmy
network mode: awsvpc
compatibility: FARGATE
cpu: 256
memory: 512
container name: ms-simulation
container port: 8080
```

Environment variables:

```text
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
RABBITMQ_PORT=5671
RABBITMQ_SSL_ENABLED=true
```

Secrets:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
RABBITMQ_HOST
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
RABBITMQ_VHOST
```

Initial desired count should be `1`.

Do not start with multiple replicas while the map read model still keeps in-memory state.

## 9. ECS Service

Create one ECS service:

```text
service name: ms-simulation-service-2026-atmy
cluster: junior-workshop-2026-vlc
launch type: FARGATE
desired count: 1
task definition: ms-simulation-2026-atmy
```

Networking:

- Use the ECS task security group.
- Use private subnets if possible.
- Ensure outbound access to ECR, CloudWatch Logs, RDS and CloudAMQP.

## 10. Load Balancer

An Application Load Balancer is optional.

If public or shared HTTP access is required, route both functional areas to the same ECS service:

```text
/tick/* -> ms-simulation target group
/map    -> ms-simulation target group
```

Health check options:

- Prefer a Spring Boot Actuator health endpoint if Actuator is added later.
- Without Actuator, use an endpoint that is safe to call repeatedly, such as `GET /tick/current`.

## 11. GitHub Actions

The repository should use one build workflow and one deploy workflow.

Build workflow:

1. Set up Java 21.
2. Run tests.
3. Build the application.
4. Authenticate to AWS through OIDC.
5. Log in to ECR.
6. Build one Docker image from the repository root.
7. Push the image to `ECR_REPOSITORY_MS_SIMULATION`.
8. Print the commit SHA as the deployable image tag.

Deploy workflow:

1. Receive `image_tag`.
2. Authenticate to AWS through OIDC.
3. Resolve the full `ms-simulation` image URI.
4. Render `deployment/ecs/ms-simulation-task-definition.json`.
5. Deploy `ECS_SERVICE_MS_SIMULATION`.
6. Wait for service stability.

## 12. Migration From The Old Two-Service Model

Recommended sequence:

1. Keep `ms-time` and `ms-map` running.
2. Create the new `ms-simulation` ECR repository.
3. Create the new task definition and ECS service.
4. Build and push the first `ms-simulation` image.
5. Deploy `ms-simulation` with desired count `1`.
6. Validate the new service.
7. Switch ALB routes, DNS or clients to the new service.
8. Stop `ms-time` and `ms-map`.
9. Delete old resources only after rollback is no longer needed.

Rollback is easiest before deleting old resources: route traffic back to `ms-time` and `ms-map`, then scale down `ms-simulation`.

## 13. Validation

Validate after deployment:

```text
GET /tick/current
POST /tick/1
GET /map
```

Also validate:

- RDS connectivity.
- CloudAMQP connectivity.
- `time.advanced.v1` publication.
- Map event consumption.
- CloudWatch logs.
- ECS service stability.

Useful checks:

```text
aws ecs describe-services --cluster junior-workshop-2026-vlc --services ms-simulation-service-2026-atmy
aws logs tail /ecs/ms-simulation-2026-atmy --follow
```
