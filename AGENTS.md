# AGENTS.md

## Project context

This repository is `MS-SIMULATION`, the Time + Map / Simulation Service of the Supply Chain Simulator Workshop.

The full system is a distributed microservices simulation of a supply chain with factories, warehouses, trucks and reporting.

This microservice has two main responsibilities:

1. Simulation clock:
    - Controls the current simulation day.
    - Exposes `POST /tick/{days}` to advance simulation time.
    - Publishes the `time.advanced.v1` event through RabbitMQ when time advances.
    - Represents simulation day as an `int`, never as `Date`, `LocalDate` or calendar time.
    - Does not move trucks, manufacture products, modify stock or generate reports.

2. Map state:
    - Maintains a read model of the current map.
    - Is not the owner of trucks, factories or warehouses.
    - Stores only the latest known position/status of map elements.
    - Is updated by consuming events from other microservices.

## Main modules

The codebase is organized by layers and by functional area:

- `simulationclock`: simulation day, time advancement and `time.advanced.v1`. You can find him in the module ms-time
- `mapstate`: map read model built from external events. You can find him in the module ms-map

The two modules must remain conceptually separated. Do not mix simulation clock logic with map state logic.

## Architecture rules

Follow Clean Architecture / Hexagonal Architecture / DDD boundaries.

Dependency direction must be:

`infrastructure -> application -> domain`

Rules:

- `domain` contains pure business logic.
- `domain` must not depend on Spring, JPA, RabbitMQ, Jackson, controllers, repositories, adapters or other frameworks.
- `application` contains use cases and ports.
- `application` must not depend directly on infrastructure adapters.
- `application` may depend on ports/interfaces.
- `infrastructure` contains REST controllers, persistence, RabbitMQ config, publishers, listeners, adapters and framework-specific code.
- Controllers and listeners must be thin. They should map external input and call application use cases.
- Do not place business rules in controllers, listeners, repositories or message DTOs.
- Do not introduce new dependencies without explaining why they are needed.

## Simulation clock rules

The simulation clock module owns only time advancement.

Expected published event:

```json
{
  "eventId": "uuid",
  "previousDayNumber": 4,
  "currentDayNumber": 5,
  "daysAdvanced": 1,
  "occurredAt": "2026-04-29T10:00:00Z"
}
````

## AWS deployment rules

The initial deployment target is basic:

- Amazon EC2 runs the Spring Boot application.
- Amazon RDS PostgreSQL provides the database.
- CloudAMQP provides RabbitMQ.
- GitHub Actions may be used for CI/CD.

Deployment work must not change business behavior.

Do not modify:

- simulation clock domain rules
- map state domain rules
- REST contracts
- RabbitMQ event contracts
- payload field names

unless the user explicitly asks.

Production configuration must use environment variables.

Never commit:

- AWS credentials
- database credentials
- CloudAMQP credentials
- SSH private keys
- tokens
- real production URLs containing credentials

Prefer these environment variables for production:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`
- `RABBITMQ_VHOST`
- `RABBITMQ_SSL_ENABLED`

A basic GitHub Actions deployment should:

1. Set up Java 21.
2. Run tests.
3. Build the Spring Boot JAR.
4. Copy the artifact to EC2.
5. Restart the application service on EC2.
6. Keep all credentials in GitHub Secrets.

Deployment documentation should clearly list:

- required AWS resources
- required GitHub secrets
- required environment variables
- EC2 setup steps
- RDS setup steps
- CloudAMQP setup steps
- validation commands