---
name: generate-tests
description: Generate or update Java tests for domain, application, infrastructure, REST controllers, RabbitMQ publishers/listeners and persistence adapters in MS-SIMULATION.
---

# Generate Tests Skill

Use this skill when creating, updating or reviewing tests.

## Testing priorities

Prefer tests in this order:

1. Domain tests for pure business rules.
2. Application use case tests with mocked ports.
3. Infrastructure tests for controllers, RabbitMQ, persistence and adapters.
4. Spring context tests only when necessary.

## General rules

- Test behavior, not implementation details.
- Keep tests easy to read.
- Use descriptive test method names.
- Use clear given/when/then structure when useful.
- Avoid unnecessary mocks.
- Do not weaken assertions just to make tests pass.
- Do not delete meaningful tests unless replacing them with stronger ones.
- If production code is modified, add or update tests in the same change.

## Domain tests

For domain tests:

- Do not use repositories, controllers or messaging.
- Test invariants, validation rules and state transitions.
- Prefer simple object creation.

## Application tests

For application use case tests:

- Mock output ports.
- Verify the use case calls the correct ports.
- Verify the returned result.
- Avoid mocking domain objects unnecessarily.

## Infrastructure tests

For infrastructure tests:

- Test request/response mapping for controllers.
- Test message mapping for RabbitMQ publishers/listeners.
- Test persistence mapping for JPA adapters/entities.
- Keep framework tests focused.

## Output format

When adding tests, provide:

1. Test files added or changed
2. Behaviors covered
3. Important edge cases
4. Validation command to run