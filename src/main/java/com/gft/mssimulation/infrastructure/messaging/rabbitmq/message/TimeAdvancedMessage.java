package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TimeAdvancedMessage(
        UUID eventId,
        int previousDay,
        int currentDay,
        int daysAdvanced,
        Instant occurredAt
) {

    public static TimeAdvancedMessage from(TimeAdvancedEvent event) {
        Objects.requireNonNull(event, "event cannot be null");

        return new TimeAdvancedMessage(
                event.eventId(),
                event.previousDay(),
                event.currentDay(),
                event.daysAdvanced(),
                event.occurredAt()
        );
    }
}
