package com.gft.mssimulation.application.simulationclock.result;

import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
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
                event.previousDay(),
                event.currentDay(),
                event.daysAdvanced(),
                event.occurredAt()
        );
    }
}