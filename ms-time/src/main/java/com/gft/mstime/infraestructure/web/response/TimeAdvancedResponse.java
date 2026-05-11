package com.gft.mstime.infraestructure.web.response;

import com.gft.mstime.application.result.TimeAdvancedResult;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TimeAdvancedResponse(
        UUID eventId,
        int previousDay,
        int currentDay,
        int daysAdvanced,
        Instant occurredAt
) {

    public static TimeAdvancedResponse from(TimeAdvancedResult result) {
        Objects.requireNonNull(result, "result cannot be null");

        return new TimeAdvancedResponse(
                result.eventId(),
                result.previousDay(),
                result.currentDay(),
                result.daysAdvanced(),
                result.occurredAt()
        );
    }
}
