# Basic AWS Deployment

This document defines the initial deployment contract for MS-SIMULATION.

The goal is a simple deployment, easy to understand and operate:

- One Amazon Linux EC2 instance.
- Two executable Spring Boot JARs.
- Two independent systemd services.
- One shared Amazon RDS PostgreSQL database.
- One shared CloudAMQP RabbitMQ broker.
- One GitHub Actions workflow that builds, tests and deploys both modules together.

## Runtime Layout

Both modules run on the same EC2 instance, but they keep separate runtime lifecycles.

| Module | JAR path | systemd service | HTTP port |
| --- | --- | --- | --- |
| `ms-time` | `/opt/ms-simulation/ms-time/ms-time.jar` | `ms-time.service` | `8081` |
| `ms-map` | `/opt/ms-simulation/ms-map/ms-map.jar` | `ms-map.service` | `8080` |

This means one module can be restarted without restarting the other one.

## Configuration Files On EC2

Production values must live outside the repository.

Expected environment files:

| Module | Environment file |
| --- | --- |
| `ms-time` | `/etc/ms-simulation/ms-time.env` |
| `ms-map` | `/etc/ms-simulation/ms-map.env` |

These files should contain values such as:

```bash
SPRING_PROFILES_ACTIVE=prod
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

Do not commit real values for database credentials, RabbitMQ credentials, AWS credentials or SSH keys.

## Infrastructure

The basic infrastructure is:

- EC2: runs both Spring Boot applications.
- RDS PostgreSQL: stores persistence data for both modules.
- CloudAMQP: provides RabbitMQ for application events.
- GitHub Actions: builds, tests, copies the JARs to EC2 and restarts both services.

RDS inbound access should be restricted to the EC2 security group whenever possible.
SSH access to EC2 should be restricted to trusted IPs.
Application ports should only be opened when they need to be reachable from outside the EC2 instance.

## GitHub Secrets

The deployment workflow should use repository or environment secrets.

Expected secrets:

```text
EC2_HOST
EC2_USER
EC2_SSH_KEY
```

If the workflow creates environment files remotely, it will also need:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
RABBITMQ_HOST
RABBITMQ_PORT
RABBITMQ_USERNAME
RABBITMQ_PASSWORD
RABBITMQ_VHOST
RABBITMQ_SSL_ENABLED
```

## Deployment Flow

The intended deployment flow is:

1. Push to `main`.
2. GitHub Actions sets up Java 21.
3. Maven runs tests for the repository.
4. Maven packages `ms-time` and `ms-map`.
5. GitHub Actions copies both JARs to EC2.
6. GitHub Actions restarts `ms-time.service`.
7. GitHub Actions restarts `ms-map.service`.

## Operational Commands

Useful EC2 commands:

```bash
sudo systemctl status ms-time
sudo systemctl status ms-map

sudo systemctl restart ms-time
sudo systemctl restart ms-map

journalctl -u ms-time -f
journalctl -u ms-map -f
```

## Validation

After deployment, validate at least:

- `ms-time` is running on port `8081`.
- `ms-map` is running on port `8080`.
- Both services can connect to RDS PostgreSQL.
- Both services can connect to CloudAMQP RabbitMQ.
- `POST /tick/{days}` still advances the simulation day.
- `time.advanced.v1` is still published with the expected payload.
