---
name: aws-ecs-fargate-deployment
description: Prepare, review or implement an AWS ECS Fargate deployment for MS-SIMULATION using Docker images, Amazon ECR, Amazon RDS PostgreSQL, CloudAMQP RabbitMQ, IAM/OIDC and GitHub Actions.
---

# AWS ECS Fargate Deployment Skill

Use this skill when preparing, reviewing or implementing the AWS deployment for MS-SIMULATION.

## Deployment scope

The deployment target uses containers and managed AWS runtime services:

- GitHub Actions builds Docker images for `ms-time` and `ms-map`.
- Amazon ECR stores the Docker images.
- Amazon ECS Fargate runs the containers.
- `ms-time` and `ms-map` are deployed as separate ECS services.
- Each module has its own Docker image, ECR repository, task definition, ECS service, container port and CloudWatch logs.
- PostgreSQL runs on Amazon RDS.
- RabbitMQ runs on CloudAMQP.
- IAM with GitHub OIDC allows GitHub Actions to access AWS without long-lived AWS access keys.

Do not use the previous EC2 + JAR + systemd deployment model unless the user explicitly asks to return to it.
Do not merge `ms-time` and `ms-map` into a single image, container or runtime process unless the user explicitly asks for that deployment model.
Do not introduce Terraform, CDK or CloudFormation unless the user explicitly asks for infrastructure as code.

## Main goals

The deployment must:

- Build and test the Java/Spring Boot application.
- Build Docker images for both `ms-time` and `ms-map`.
- Push both images to Amazon ECR.
- Deploy `ms-time` and `ms-map` as separate ECS Fargate services.
- Keep both modules separate at runtime.
- Configure PostgreSQL through environment variables or ECS secrets.
- Configure CloudAMQP through environment variables or ECS secrets.
- Avoid hardcoded credentials.
- Keep production configuration separate from local configuration.
- Send container logs to CloudWatch Logs.

## AWS components

Expected components:

- ECR repository for `ms-time`.
- ECR repository for `ms-map`.
- ECS cluster using Fargate.
- ECS task definition for `ms-time`.
- ECS task definition for `ms-map`.
- ECS service for `ms-time`.
- ECS service for `ms-map`.
- RDS PostgreSQL instance for persistence.
- Security Group for ECS tasks.
- Security Group for RDS.
- CloudWatch log group or log groups for container logs.
- IAM role assumable by GitHub Actions through OIDC.

Optional components:

- Application Load Balancer if HTTP endpoints must be reachable from outside ECS.
- AWS Secrets Manager or SSM Parameter Store for sensitive runtime values.

Security group rules:

- RDS inbound access should be limited to the ECS task Security Group whenever possible.
- Public HTTP access should go through an ALB when needed.
- ECS tasks need outbound access to ECR, CloudWatch Logs, RDS and CloudAMQP.

## CloudAMQP rules

RabbitMQ is provided by CloudAMQP.

Rules:

- Do not create local RabbitMQ infrastructure for production.
- Do not hardcode CloudAMQP credentials.
- Prefer ECS secrets for CloudAMQP credentials when possible.
- If the current Spring Boot configuration requires separate RabbitMQ values, map CloudAMQP values into:
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

The application should be configurable with variables such as:

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

Default ports:

- `ms-time`: `8081`
- `ms-map`: `8080`

## Docker rules

Dockerfiles should:

- Build or run one module per image.
- Use Java 21.
- Avoid copying unnecessary files into the final image.
- Avoid embedding secrets.
- Expose the correct container port for each module.
- Prefer a simple and readable setup over unnecessary optimization.

Use separate images for:

- `ms-time`
- `ms-map`

## GitHub Actions rules

A deployment workflow should:

1. Trigger on push to the deployment branch, usually `main`.
2. Set up Java 21.
3. Cache Maven dependencies if useful.
4. Run tests before building images.
5. Authenticate to AWS using GitHub OIDC.
6. Log in to Amazon ECR.
7. Build the `ms-time` Docker image.
8. Build the `ms-map` Docker image.
9. Push both images to ECR.
10. Render or update ECS task definitions with the new image tags.
11. Deploy both ECS services.
12. Avoid printing secrets in logs.

Expected GitHub variables or secrets may include:

- `AWS_REGION`
- `AWS_ROLE_TO_ASSUME`
- `ECR_REPOSITORY_MS_TIME`
- `ECR_REPOSITORY_MS_MAP`
- `ECS_CLUSTER`
- `ECS_SERVICE_MS_TIME`
- `ECS_SERVICE_MS_MAP`
- `ECS_TASK_DEFINITION_MS_TIME`
- `ECS_TASK_DEFINITION_MS_MAP`

Use repository variables for non-sensitive identifiers when possible.
Use GitHub secrets only for sensitive values that cannot be handled through AWS IAM, Secrets Manager or SSM.
Do not use long-lived AWS access keys unless the user explicitly accepts that tradeoff.

## ECS task definition rules

When creating or reviewing ECS task definitions, check:

- Fargate compatibility.
- CPU and memory are reasonable for a basic Spring Boot service.
- Container image points to ECR.
- Container port is correct.
- `SPRING_PROFILES_ACTIVE=prod` is set.
- Database and RabbitMQ values are injected as environment variables or secrets.
- CloudWatch Logs are configured.
- Task execution role can pull from ECR and write logs.
- Task role has only the permissions needed by the application.

## Manual AWS setup

Manual setup is acceptable and expected unless the user asks for infrastructure as code.

Document any required manual setup clearly:

- Create ECR repositories.
- Configure GitHub OIDC provider and IAM role.
- Create ECS cluster.
- Create ECS task definitions.
- Create ECS services.
- Create or configure RDS PostgreSQL.
- Configure CloudAMQP.
- Configure security groups.
- Configure optional ALB and target groups.
- Configure CloudWatch Logs.
