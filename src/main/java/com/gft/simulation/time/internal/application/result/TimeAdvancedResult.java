package com.gft.simulation.time.internal.application.result;

import com.gft.simulation.time.internal.domain.TimeAdvancedEvent;

import java.time.Instant;
import java.util.UUID;

public record TimeAdvancedResult(
        UUID eventId,
        int previousDay,
        int currentDay,
        int daysAdvanced,
        Instant occurredAt
) {

    public static TimeAdvancedResult from(TimeAdvancedEvent event) {
        return new TimeAdvancedResult(
                event.eventId(),
                event.previousDay().dayNumber(),
                event.currentDay().dayNumber(),
                event.daysAdvanced(),
                event.occurredAt()
        );
    }
}
