---
name: basic-aws-deployment
description: Prepare, review or implement a basic AWS deployment for MS-SIMULATION using EC2, separate Spring Boot JARs for ms-time and ms-map, Amazon RDS PostgreSQL, CloudAMQP RabbitMQ and GitHub Actions.
---

# Basic AWS Deployment Skill

Use this skill when preparing, reviewing or implementing a basic AWS deployment for MS-SIMULATION.

## Deployment scope

The initial deployment target is intentionally simple:

- Application runs on Amazon EC2.
- `ms-time` and `ms-map` are deployed as separate executable Spring Boot JARs.
- The basic deployment may run both modules on the same EC2 instance.
- Each module should have its own systemd service, typically `ms-time.service` and `ms-map.service`.
- PostgreSQL runs on Amazon RDS.
- RabbitMQ runs on CloudAMQP.
- CI/CD is handled with GitHub Actions.
- Deployment may use executable Spring Boot JARs and systemd services on EC2.
- Docker may be proposed only if it clearly simplifies the deployment or the user asks for it.

Do not design a complex cloud architecture unless the user explicitly asks for it.
Do not merge `ms-time` and `ms-map` into a single JAR unless the user explicitly asks for that deployment model.

## Main goals

The deployment must:

- Build the Java/Spring Boot application.
- Run tests before deployment.
- Package both `ms-time` and `ms-map` with Maven when deploying the full basic stack.
- Deploy the generated artifacts to EC2.
- Restart each application safely on EC2.
- Configure PostgreSQL through environment variables.
- Configure CloudAMQP through environment variables.
- Avoid hardcoded credentials.
- Keep production configuration separate from local configuration.
- Keep `ms-time` and `ms-map` runtime concerns separate: service names, ports, working directories, environment files and logs.

## AWS components

Expected basic components:

- EC2 instance for running the Spring Boot service.
- The EC2 instance can run both `ms-time` and `ms-map` for the basic deployment.
- RDS PostgreSQL instance for persistence.
- Security Group for EC2.
- Security Group for RDS.
- RDS inbound access should be limited to the EC2 Security Group whenever possible.
- SSH access to EC2 should be limited to trusted IPs.
- Application port should be opened only if needed.

## CloudAMQP rules

RabbitMQ is provided by CloudAMQP.

Rules:

- Do not create local RabbitMQ infrastructure for production.
- Do not hardcode CloudAMQP credentials.
- Prefer a single environment variable such as `CLOUDAMQP_URL` when possible.
- If the current Spring Boot configuration requires separate RabbitMQ values, map CloudAMQP values into:
    - `RABBITMQ_HOST`
    - `RABBITMQ_PORT`
    - `RABBITMQ_USERNAME`
    - `RABBITMQ_PASSWORD`
    - `RABBITMQ_VHOST`
    - `RABBITMQ_SSL_ENABLED`

## Spring Boot configuration rules

Production configuration must use environment variables.

Do not commit real values for:

- database URL
- database username
- database password
- RabbitMQ host
- RabbitMQ username
- RabbitMQ password
- CloudAMQP URL
- SSH keys
- AWS credentials

The application should be configurable with variables such as:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`
- `RABBITMQ_VHOST`
- `RABBITMQ_SSL_ENABLED`

## GitHub Actions rules

A basic deployment workflow should:

1. Trigger on push to the deployment branch, usually `main` or `master`.
2. Set up Java 21.
3. Cache Maven dependencies if useful.
4. Run `./mvnw test` or `mvnw.cmd test` depending on runner.
5. Package `ms-time` and `ms-map` with Maven.
6. Copy both built JARs to EC2.
7. Restart `ms-time.service` and `ms-map.service` on EC2.
8. Avoid printing secrets in logs.

GitHub secrets may include:

- `EC2_HOST`
- `EC2_USER`
- `EC2_SSH_KEY`
- `EC2_APP_PATH`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`
- `RABBITMQ_VHOST`
- `RABBITMQ_SSL_ENABLED`

Use repository or environment secrets. Do not commit secret values.

## EC2 service rules

If using systemd, provide or review:

- separate service names for `ms-time` and `ms-map`
- separate working directories
- separate JAR paths
- environment file paths
- restart policy
- logs through `journalctl`

The applications should be restartable with commands similar to:

```bash
sudo systemctl restart ms-time
sudo systemctl restart ms-map
```
