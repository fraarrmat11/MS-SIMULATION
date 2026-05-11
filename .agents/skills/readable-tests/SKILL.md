---
name: readable-tests
description: Make Java tests readable and maintainable with descriptive names, clear given/when/then structure, behavior-focused assertions and minimal mocking.
---

# Readable Tests Skill

Use this skill when creating, updating or reviewing tests.

## Test readability rules

- Test names must describe behavior.
- Prefer names like `shouldPublishTimeAdvancedEventWhenClockAdvances`.
- Avoid vague names like `test1`, `shouldWork` or `validTest`.
- Use given/when/then comments when they improve readability.
- Keep test setup short.
- Extract test data builders only when they reduce duplication.
- Avoid over-engineered test helpers.
- Assertions should be clear and specific.
- Avoid multiple unrelated behaviors in one test.

## Behavior focus

Tests should answer:

- Given this initial situation,
- When this action happens,
- Then this observable result should occur.

Prefer testing observable behavior over internal implementation details.

## Mocking rules

- Mock output ports in application tests.
- Avoid verifying every internal call.
- Verify interactions only when the interaction is part of the behavior.

## Test data

- Use meaningful values.
- Avoid random values unless randomness is the behavior under test.
- Use constants when they clarify intent.
- Keep UUIDs, names and locations readable.

## Output format

When reviewing tests, return:

1. Tests that are clear
2. Tests that are hard to read
3. Naming improvements
4. Setup simplification opportunities
5. Missing behavior cases