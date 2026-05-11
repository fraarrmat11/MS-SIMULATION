---
name: document-module
description: Document a module, use case, REST endpoint, RabbitMQ event or architecture flow in MS-SIMULATION clearly and technically.
---

# Document Module Skill

Use this skill when writing or updating documentation.

## Documentation goals

Documentation should help a developer understand:

- What the module does
- What the module does not do
- Main classes and responsibilities
- Main flow
- REST endpoints
- RabbitMQ events
- Architecture boundaries
- Testing approach

## Style

- Be clear, technical and concise.
- Prefer structured sections.
- Use examples when documenting payloads.
- Avoid vague statements.
- Avoid documenting obvious code line by line.
- Explain responsibilities and boundaries.
- Highlight contract impacts.

## Required sections when documenting a module

Use these sections when applicable:

1. Purpose
2. Responsibilities
3. Non-responsibilities
4. Main flow
5. Main classes
6. API
7. Events published
8. Events consumed
9. Architecture notes
10. Testing notes

## Project-specific rules

For `simulationclock`:

- Mention that simulation day is an `int`.
- Mention that this service only informs that time advanced.
- Mention `time.advanced.v1`.

For `mapstate`:

- Mention that it is a read model.
- Mention that it does not own trucks, warehouses or factories.
- Mention that it stores the latest known state from external events.

## Output

When updating docs, summarize:

1. Files changed
2. Sections added or updated
3. Any contract assumptions documented