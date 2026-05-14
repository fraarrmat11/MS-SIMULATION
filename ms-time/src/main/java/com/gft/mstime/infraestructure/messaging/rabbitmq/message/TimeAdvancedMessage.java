package com.gft.mstime.infraestructure.messaging.rabbitmq.message;

import com.gft.mstime.domain.TimeAdvancedEvent;

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
                event.previousDay().dayNumber(),
                event.currentDay().dayNumber(),
                event.daysAdvanced(),
                event.occurredAt()
        );
    }
}
