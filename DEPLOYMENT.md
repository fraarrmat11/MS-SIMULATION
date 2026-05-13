# AWS ECS Fargate Deployment

This document defines the deployment contract for MS-SIMULATION.

The deployment target is based on containers and managed AWS runtime services:

- GitHub Actions builds Docker images for `ms-time` and `ms-map`.
- Amazon ECR stores the Docker images.
- Amazon ECS Fargate runs the containers.
- `ms-time` and `ms-map` run as separate ECS services.
- Amazon RDS PostgreSQL provides the shared database.
- CloudAMQP provides the shared RabbitMQ broker.
- IAM with GitHub OIDC grants GitHub Actions access to AWS without long-lived AWS keys.

This replaces the previous EC2 + JAR + systemd deployment approach.

## Runtime Layout

Both modules remain separate runtime units.

| Module | ECR image | ECS service | Container port |
| --- | --- | --- | --- |
| `ms-time` | `<aws-account-id>.dkr.ecr.<region>.amazonaws.com/ms-time` | `ms-time-service` | `8081` |
| `ms-map` | `<aws-account-id>.dkr.ecr.<region>.amazonaws.com/ms-map` | `ms-map-service` | `8080` |

Each module should have its own:

- Docker image.
- ECR repository.
- ECS task definition.
- ECS service.
- CloudWatch log group.
- Container port.

The services may run in the same ECS cluster.

## Infrastructure

The expected AWS and external components are:

- ECR repository for `ms-time`.
- ECR repository for `ms-map`.
- ECS cluster using Fargate.
- ECS task definition for `ms-time`.
- ECS task definition for `ms-map`.
- ECS service for `ms-time`.
- ECS service for `ms-map`.
- RDS PostgreSQL database shared by both modules.
- CloudAMQP RabbitMQ broker shared by both modules.
- CloudWatch Logs for container logs.
- IAM role assumable from GitHub Actions through OIDC.

An Application Load Balancer is optional. Use it if the HTTP endpoints must be reachable from outside ECS. If the services are only consumed internally, expose only the minimum networking required.

Manual AWS setup is acceptable for this project. The repository should document what must be created, but it should not require Terraform, CDK or CloudFormation unless explicitly requested.

## Configuration

Production values must come from environment variables or AWS-managed secrets injected into the ECS task definitions.

Expected runtime variables:

```text
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=<module-port>

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

Recommended port values:

| Module | `SERVER_PORT` |
| --- | --- |
| `ms-time` | `8081` |
| `ms-map` | `8080` |

Do not commit real database credentials, RabbitMQ credentials, AWS credentials, CloudAMQP URLs or private keys.

## GitHub Actions

GitHub Actions should:

1. Trigger on push to `main`.
2. Set up Java 21.
3. Run the Maven test suite.
4. Build Docker images for `ms-time` and `ms-map`.
5. Authenticate to AWS through GitHub OIDC.
6. Push both images to ECR.
7. Render or update ECS task definitions with the new image tags.
8. Deploy both ECS services.

Prefer GitHub OIDC over long-lived `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` secrets.

Expected GitHub configuration:

```text
AWS_REGION
AWS_ROLE_TO_ASSUME
ECR_REPOSITORY_MS_TIME
ECR_REPOSITORY_MS_MAP
ECS_CLUSTER
ECS_SERVICE_MS_TIME
ECS_SERVICE_MS_MAP
ECS_TASK_DEFINITION_MS_TIME
ECS_TASK_DEFINITION_MS_MAP
```

These may be GitHub repository variables or environment variables. Sensitive values should be GitHub secrets or stored in AWS Secrets Manager/SSM Parameter Store and referenced by ECS.

## Networking And Security

Recommended security model:

- ECS tasks run in private subnets when possible.
- ECS tasks have outbound access to ECR, CloudWatch Logs, RDS and CloudAMQP.
- RDS inbound access is restricted to the ECS task security group.
- Public HTTP access goes through an ALB if required.
- GitHub Actions uses IAM/OIDC and only receives the minimum AWS permissions needed for ECR push and ECS deploy.

## Validation

After deployment, validate at least:

- `ms-time` ECS task is running and healthy.
- `ms-map` ECS task is running and healthy.
- Both services can connect to RDS PostgreSQL.
- Both services can connect to CloudAMQP RabbitMQ.
- `ms-time` responds on port `8081` through the chosen access path.
- `ms-map` responds on port `8080` through the chosen access path.
- `POST /tick/{days}` still advances the simulation day.
- `time.advanced.v1` is still published with the expected payload.
- Container logs are visible in CloudWatch Logs.
