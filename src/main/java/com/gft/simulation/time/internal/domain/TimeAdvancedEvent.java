package com.gft.simulation.time.internal.domain;

import com.gft.simulation.time.internal.domain.exceptions.InvalidTimeAdvanceException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class TimeAdvancedEvent {

    private final UUID eventId;
    private final SimulationDay previousDay;
    private final SimulationDay currentDay;
    private final Instant occurredAt;

    private TimeAdvancedEvent(UUID eventId, SimulationDay previousDay, SimulationDay currentDay, Instant occurredAt) {
        Objects.requireNonNull(previousDay, "previousDay cannot be null");
        Objects.requireNonNull(currentDay, "currentDay cannot be null");

        if (!previousDay.isBefore(currentDay)) {
            throw new InvalidTimeAdvanceException(previousDay, currentDay);
        }

        this.eventId = Objects.requireNonNull(eventId, "eventId cannot be null");
        this.previousDay = previousDay;
        this.currentDay = currentDay;
        this.occurredAt = Objects.requireNonNull(occurredAt, "ocurredAt cannot be null");
    }

    public static TimeAdvancedEvent of(SimulationDay previousDay, SimulationDay currentDay) {
        return new TimeAdvancedEvent(UUID.randomUUID(), previousDay, currentDay, Instant.now());
    }

    public UUID eventId() {
        return eventId;
    }

    public SimulationDay previousDay() {
        return previousDay;
    }

    public SimulationDay currentDay() {
        return currentDay;
    }

    public int daysAdvanced() {
        return currentDay.dayNumber() - previousDay.dayNumber();
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeAdvancedEvent other)) return false;
        return eventId.equals(other.eventId);
    }

    @Override
    public int hashCode() {
        return eventId.hashCode();
    }
}
