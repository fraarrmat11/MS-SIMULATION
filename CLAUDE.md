# MS-SIMULATION — Claude Code Guide

## Project overview

Spring Boot modulith with two bounded contexts:

- **map** — read model of the current map state (truck positions, warehouse positions). Updated from external RabbitMQ events. Does not own trucks or warehouses.
- **time** — owns the simulation day. Advances time via `POST /tick/{days}` and publishes `time.advanced.v1`.

## Architecture rules (always enforced)

Dependency direction: `infrastructure → application → domain`

- Domain must not import Spring, JPA, RabbitMQ, Jackson, or any framework code.
- Application layer depends only on ports/interfaces, never on adapters.
- Controllers, listeners, and JPA adapters must not contain business logic.
- The two modules (map, time) must not import each other's internals. Cross-module access only via public Facade.

## Package structure

```
com.gft.simulation.{module}.internal.domain          — entities, value objects, domain exceptions
com.gft.simulation.{module}.internal.application     — use cases, services, port interfaces
com.gft.simulation.{module}.internal.infrastructure  — JPA adapters, RabbitMQ listeners/publishers, controllers
com.gft.simulation.{module}.*Facade                  — public API of the module
```

## RabbitMQ contracts

Published: `time.advanced.v1`

Consumed: `truck.registered.v1`, `truck.position.updated.v1`, `truck.deleted.v1`, `warehouse.registered.v1`

## Coding conventions

- No comments unless the WHY is non-obvious (hidden constraint, workaround, subtle invariant).
- No Co-Authored-By trailers in commits.
- Exception handlers scoped to their own module's controller via `assignableTypes`.
- Use cases persist to DB first, then update in-memory state.
- `@Transactional` belongs on use case `execute` methods, not on individual adapter methods.

## Specialist roles

Invoke with: **"actúa como [role]"** or **"usa el rol de [role]"**.

### architecture_reviewer
@.codex/agents/architecture_reviewer.toml

### test_engineer
@.codex/agents/test_engineer.toml

### simulation_clock_specialist
@.codex/agents/simulation_clock_specialist.toml

### rabbitmq_contract_reviewer
@.codex/agents/rabbitmq_contract_reviewer.toml

### aws_deployment_engineer
@.codex/agents/aws_deployment_engineer.toml

### documentation_writer
@.codex/agents/documentation_writer.toml
