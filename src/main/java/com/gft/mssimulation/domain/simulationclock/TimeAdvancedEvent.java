package com.gft.mssimulation.domain.simulationclock;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class TimeAdvancedEvent {

    private final UUID eventId;
    private final int previousDay;
    private final int currentDay;
    private final int daysAdvanced;
    private final Instant occurredAt;

    private TimeAdvancedEvent(
            UUID eventId,
            int previousDayNumber,
            int currentDayNumber,
            int daysAdvanced,
            Instant occurredAt
    ) {
        if (previousDayNumber < 0) {
            throw new IllegalArgumentException("Previous day cannot be negative");
        }
        if (currentDayNumber < 0) {
            throw new IllegalArgumentException("Current day cannot be negative");
        }
        if (daysAdvanced <= 0) {
            throw new IllegalArgumentException("Days advanced must be greater than zero");
        }
        if (currentDayNumber != previousDayNumber + daysAdvanced) {
            throw new IllegalArgumentException("Current day must match previous day plus days advanced");
        }

        this.eventId = Objects.requireNonNull(eventId, "eventId cannot be null");
        this.previousDay = previousDayNumber;
        this.currentDay = currentDayNumber;
        this.daysAdvanced = daysAdvanced;
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
    }

    public static TimeAdvancedEvent occurred(int previousDayNumber, int currentDayNumber, int daysAdvanced) {
        return new TimeAdvancedEvent(
                UUID.randomUUID(),
                previousDayNumber,
                currentDayNumber,
                daysAdvanced,
                Instant.now()
        );
    }

    public UUID eventId() {
        return eventId;
    }

    public int previousDay() {
        return previousDay;
    }

    public int currentDay() {
        return currentDay;
    }

    public int daysAdvanced() {
        return daysAdvanced;
    }

    public Instant occurredAt() {
        return occurredAt;
    }
}
