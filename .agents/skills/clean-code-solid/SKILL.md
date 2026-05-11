---
name: clean-code-solid
description: >-
  Apply Clean Code and SOLID principles when generating, reviewing, or refactoring Java production code. Use for descriptive names, small methods, single responsibility, low coupling, high cohesion, no duplication, and clear architecture boundaries.
---

# Clean Code and SOLID Skill

Use this skill whenever generating, reviewing or refactoring production code.

## Clean Code rules

- Use names that reveal intent.
- Prefer explicit domain language over generic names.
- Keep methods small.
- Keep methods focused on one responsibility.
- Avoid deep nesting when a guard clause improves readability.
- Avoid duplication.
- Avoid unnecessary comments.
- Prefer self-documenting code.
- Do not hide complex logic inside unclear helper methods.
- Do not introduce clever code when simple code is enough.

## SOLID rules

### Single Responsibility Principle

Each class should have one clear reason to change.

Examples:

- Controllers handle HTTP mapping, not business decisions.
- Listeners handle message mapping, not business decisions.
- Use cases orchestrate application flow.
- Domain objects enforce domain rules.
- Repositories/adapters handle persistence.

### Open/Closed Principle

Prefer extension through ports/interfaces when crossing architectural boundaries, but do not create abstractions without a real need.

### Liskov Substitution Principle

Do not create inheritance hierarchies unless substitutions are safe and meaningful. Prefer composition.

### Interface Segregation Principle

Keep ports small and specific to the use case.

### Dependency Inversion Principle

Application should depend on ports/interfaces. Infrastructure should implement those ports.

## Architecture-specific checks

- `domain` must not import Spring, JPA, RabbitMQ, Jackson or infrastructure.
- `application` must not import infrastructure adapters.
- `infrastructure` may depend on application and domain.
- Keep `simulationclock` and `mapstate` separated.

## Review output

When reviewing code quality, return:

1. Issue
2. File/class/method
3. Why it matters
4. Suggested improvement
5. Whether it is required now or can wait
