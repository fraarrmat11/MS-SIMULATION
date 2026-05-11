---
name: safe-refactor
description: Refactor Java/Spring code safely without changing external behavior, REST contracts, RabbitMQ contracts or domain rules.
---

# Safe Refactor Skill

Use this skill when refactoring existing code.

## Main rule

Refactor without changing behavior unless the user explicitly asks for a behavior change.

## Before editing

Before changing files:

1. Identify the current behavior.
2. Identify affected layer: domain, application, infrastructure, tests or docs.
3. Explain the intended refactor.
4. State whether REST or RabbitMQ contracts are affected.
5. Prefer small, reviewable steps.

## Refactor rules

- Preserve public behavior.
- Preserve REST endpoints unless explicitly asked.
- Preserve RabbitMQ event names, routing keys and payload fields unless explicitly asked.
- Do not move business logic into infrastructure.
- Do not make application depend on infrastructure.
- Do not add abstractions unless they remove a real dependency or duplication.
- Prefer renaming for clarity when it improves intent.
- Remove unused imports and dead code.
- Keep modules `simulationclock` and `mapstate` separated.

## After editing

After editing:

1. Summarize changed files.
2. Explain why behavior is preserved.
3. Explain how architecture improved or stayed valid.
4. Mention tests added or updated.
5. Recommend running `./mvnw test` or `./mvnw verify`.

## Stop conditions

Stop and ask for user decision if:

- A required change would alter a public contract.
- A required change would affect another microservice.
- A required change would reduce test coverage.
- A required change would require adding a production dependency.