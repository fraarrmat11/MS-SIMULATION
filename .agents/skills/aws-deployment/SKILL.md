---
name: aws-ecs-fargate-deployment
description: Prepare, review or implement an AWS ECS Fargate deployment for MS-SIMULATION using one Spring Boot Modulith Docker image, Amazon ECR, Amazon ECS Fargate, Amazon RDS PostgreSQL, CloudAMQP RabbitMQ, IAM/OIDC and GitHub Actions.
---

# AWS ECS Fargate Deployment Skill

Use this skill when preparing, reviewing or implementing the AWS deployment for MS-SIMULATION.

## Current deployment model

MS-SIMULATION is now a Spring Modulith application deployed as one runtime unit:

- One Maven project.
- One Spring Boot main class: `com.gft.simulation.SimulationApplication`.
- One executable JAR.
- One Docker image.
- One ECR repository.
- One ECS task definition.
- One ECS Fargate service.

The codebase still contains two application modules:

- `time`: simulation clock, `POST /tick/{days}`, `GET /tick/current`, and `time.advanced.v1` publication.
- `map`: map read model, `GET /map`, and RabbitMQ listeners for external map events.

These modules must remain conceptually separated in code, but they must not be deployed as separate runtime services unless the user explicitly asks to return to the old model.

## Deployment scope

The deployment target uses containers and managed AWS runtime services:

- GitHub Actions builds and tests the Java/Spring Boot application.
- GitHub Actions builds one Docker image for `ms-simulation`.
- Amazon ECR stores the `ms-simulation` Docker image.
- Amazon ECS Fargate runs one `ms-simulation` service.
- PostgreSQL runs on Amazon RDS.
- RabbitMQ runs on CloudAMQP.
- IAM with GitHub OIDC allows GitHub Actions to access AWS without long-lived AWS access keys.
- CloudWatch Logs stores the container logs.

Do not use the previous EC2 + JAR + systemd deployment model unless the user explicitly asks to return to it.
Do not recreate the old `ms-time` and `ms-map` split-service ECS model unless the user explicitly asks for that rollback path.
Do not introduce Terraform, CDK or CloudFormation unless the user explicitly asks for infrastructure as code.

## Main goals

The deployment must:

- Build and test the Java/Spring Boot application.
- Build one Docker image for `ms-simulation` from the repository root.
- Push the image to Amazon ECR.
- Deploy `ms-simulation` as one ECS Fargate service.
- Configure PostgreSQL through environment variables or ECS secrets.
- Configure CloudAMQP through environment variables or ECS secrets.
- Avoid hardcoded credentials.
- Keep production configuration separate from local configuration.
- Send container logs to CloudWatch Logs.
- Preserve the existing REST contracts and RabbitMQ event contracts unless the user explicitly asks to change them.

## AWS components

Expected components:

- ECR repository for `ms-simulation`.
- ECS cluster using Fargate.
- ECS task definition for `ms-simulation`.
- ECS service for `ms-simulation`.
- RDS PostgreSQL instance for persistence.
- Security group for ECS tasks.
- Security group for RDS.
- CloudWatch log group for container logs.
- IAM role assumable by GitHub Actions through OIDC.

Optional components:

- Application Load Balancer if HTTP endpoints must be reachable from outside ECS.
- AWS Secrets Manager or SSM Parameter Store for sensitive runtime values.

Security group rules:

- RDS inbound access should be limited to the ECS task security group whenever possible.
- Public HTTP access should go through an ALB when needed.
- ECS tasks need outbound access to ECR, CloudWatch Logs, RDS and CloudAMQP.

## Naming conventions

Prefer these names unless the user gives different AWS resource names:

- ECR repository: `ms-simulation-2026-atmy`
- ECS task definition family: `ms-simulation-2026-atmy`
- ECS service: `ms-simulation-service-2026-atmy`
- ECS container name: `ms-simulation`
- CloudWatch log group: `/ecs/ms-simulation-2026-atmy`
- Container port: `8080`

Old names such as `ms-time`, `ms-map`, `ECR_REPOSITORY_MS_TIME`, `ECR_REPOSITORY_MS_MAP`, `ECS_SERVICE_MS_TIME` and `ECS_SERVICE_MS_MAP` belong to the previous split-service deployment model.

## CloudAMQP rules

RabbitMQ is provided by CloudAMQP.

Rules:

- Do not create local RabbitMQ infrastructure for production.
- Do not hardcode CloudAMQP credentials.
- Prefer ECS secrets for CloudAMQP credentials when possible.
- Map CloudAMQP values into:
  - `RABBITMQ_HOST`
  - `RABBITMQ_PORT`
  - `RABBITMQ_USERNAME`
  - `RABBITMQ_PASSWORD`
  - `RABBITMQ_VHOST`
  - `RABBITMQ_SSL_ENABLED`

## Spring Boot configuration rules

Production configuration must use environment variables or ECS-injected secrets.

Do not commit real values for:

- database URL
- database username
- database password
- RabbitMQ host
- RabbitMQ username
- RabbitMQ password
- CloudAMQP URL
- AWS credentials
- AWS account-specific secrets
- private keys

The application should be configurable with:

- `SPRING_PROFILES_ACTIVE`
- `SERVER_PORT`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`
- `RABBITMQ_VHOST`
- `RABBITMQ_SSL_ENABLED`

Recommended production values:

- `SPRING_PROFILES_ACTIVE=prod`
- `SERVER_PORT=8080`
- `RABBITMQ_PORT=5671`
- `RABBITMQ_SSL_ENABLED=true`

## Docker rules

Dockerfiles should:

- Build or run the single `ms-simulation` application.
- Use Java 21.
- Avoid copying unnecessary files into the final image.
- Avoid embedding secrets.
- Expose container port `8080`.
- Prefer a simple and readable setup over unnecessary optimization.

Use one image:

- `ms-simulation`

Do not build images from `./ms-time` or `./ms-map`; those module directories are no longer part of the current repository layout.

## GitHub Actions rules

A build workflow should:

1. Trigger manually with `workflow_dispatch`, unless the user asks for automatic deployment.
2. Set up Java 21.
3. Cache Maven dependencies if useful.
4. Run tests before building the image.
5. Build the Spring Boot JAR.
6. Authenticate to AWS using GitHub OIDC.
7. Log in to Amazon ECR.
8. Build one `ms-simulation` Docker image from the repository root.
9. Push the image to ECR using the commit SHA as the image tag.
10. Print the image tag needed by the deployment workflow.
11. Avoid printing secrets in logs.

A deploy workflow should:

1. Trigger manually with `workflow_dispatch`.
2. Receive an explicit `image_tag`.
3. Authenticate to AWS using GitHub OIDC.
4. Resolve the `ms-simulation` ECR image URI.
5. Render the `ms-simulation` ECS task definition with the new image.
6. Deploy the `ms-simulation` ECS service.
7. Wait for service stability.

Expected GitHub variables or secrets may include:

- `AWS_REGION`
- `AWS_ROLE_TO_ASSUME`
- `ECR_REPOSITORY_MS_SIMULATION`
- `ECS_CLUSTER`
- `ECS_SERVICE_MS_SIMULATION`
- `ECS_TASK_DEFINITION_MS_SIMULATION`

Use repository variables for non-sensitive identifiers when possible.
Use GitHub secrets only for sensitive values that cannot be handled through AWS IAM, Secrets Manager or SSM.
Do not use long-lived AWS access keys unless the user explicitly accepts that tradeoff.

## ECS task definition rules

When creating or reviewing the ECS task definition, check:

- Fargate compatibility.
- CPU and memory are reasonable for a basic Spring Boot service.
- Container image points to the `ms-simulation` ECR repository.
- Container name is `ms-simulation`.
- Container port is `8080`.
- `SPRING_PROFILES_ACTIVE=prod` is set.
- `SERVER_PORT=8080` is set.
- Database and RabbitMQ values are injected as environment variables or secrets.
- CloudWatch Logs are configured.
- Task execution role can pull from ECR, write logs and read ECS-injected secrets.
- Task role has only the permissions needed by the application.

Recommended initial ECS desired count: `1`.

Reason: the map read model keeps in-memory state. Running several replicas without redesigning map state synchronization can make RabbitMQ listeners distribute events across instances and leave each instance with a different map view.

## Manual AWS setup

Manual setup is acceptable and expected unless the user asks for infrastructure as code.

Document any required manual setup clearly:

- Create the `ms-simulation` ECR repository.
- Configure GitHub OIDC provider and IAM role.
- Create or reuse the ECS cluster.
- Create the `ms-simulation` task definition.
- Create the `ms-simulation` ECS service.
- Create or configure RDS PostgreSQL.
- Configure CloudAMQP.
- Configure security groups.
- Configure optional ALB and target group.
- Configure CloudWatch Logs.

## Migration rules

When migrating from the old two-service deployment:

1. Keep `ms-time` and `ms-map` running until `ms-simulation` is validated.
2. Create new `ms-simulation` resources in parallel.
3. Deploy `ms-simulation` with desired count `1`.
4. Validate HTTP endpoints, RDS connectivity, RabbitMQ publishing and RabbitMQ consumption.
5. Switch ALB routes, DNS or clients to `ms-simulation`.
6. Stop old `ms-time` and `ms-map` services.
7. Delete old resources only after rollback is no longer needed.

Rollback is simplest before deleting old resources: route traffic back to the old services, then scale down `ms-simulation`.
