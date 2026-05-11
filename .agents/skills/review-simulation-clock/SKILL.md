---
name: review-simulation-clock
description: Review the simulation clock module, time advancement domain logic, persistence flow, time.advanced.v1 publication and related tests. Do not check or require GET /tick/current-day.
---

# Review Simulation Clock Skill

Use this skill when reviewing or improving the simulation clock module.

## Scope

Review only the simulation time responsibility:

- Simulation day model
- Time advancement use case
- `POST/tick/{days}`
- Persistence of current simulation day
- `time.advanced.v1` domain event
- RabbitMQ publication of `time.advanced.v1`
- Tests related to simulation clock

## Domain checks

Verify that:

- Simulation day is represented as `int`.
- No calendar date type is used to represent the simulation day.
- Days cannot be negative.
- Days advanced must be greater than zero.
- Advancing time from day N by X days produces day N + X.
- Domain code does not depend on Spring, JPA, RabbitMQ, Jackson or infrastructure classes.

## Application checks

Verify that:

- The use case loads the current simulation clock.
- The use case advances the clock through domain behavior.
- The use case persists the updated clock.
- The use case publishes the resulting event.
- Application code depends on ports/interfaces, not infrastructure adapters.

## Event checks

Verify that `time.advanced.v1` contains the expected information:

- `eventId`
- `previousDayNumber`
- `currentDayNumber`
- `daysAdvanced`
- `occurredAt`

Check naming carefully. If the code uses different field names, report the mismatch and contract impact.

## Output format

Return:

1. Summary
2. Files/classes reviewed
3. Correct behavior found
4. Problems found
5. Contract risks
6. Missing or weak tests
7. Recommended next steps

Do not edit code unless the user explicitly asks for implementation.