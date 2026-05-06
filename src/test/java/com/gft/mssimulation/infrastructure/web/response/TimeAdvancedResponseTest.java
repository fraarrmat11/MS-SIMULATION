package com.gft.mssimulation.infrastructure.web.response;

import com.gft.mssimulation.application.simulationclock.result.TimeAdvancedResult;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeAdvancedResponseTest {

    @Test
    void from_WhenGivenTimeAdvancedResult_ShouldMapAllFields() {
        UUID eventId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Instant occurredAt = Instant.parse("2026-05-04T11:30:00Z");
        TimeAdvancedResult result = new TimeAdvancedResult(eventId, 0, 1, 1, occurredAt);

        TimeAdvancedResponse response = TimeAdvancedResponse.from(result);

        assertThat(response.eventId()).isEqualTo(result.eventId());
        assertThat(response.previousDay()).isEqualTo(result.previousDay());
        assertThat(response.currentDay()).isEqualTo(result.currentDay());
        assertThat(response.daysAdvanced()).isEqualTo(result.daysAdvanced());
        assertThat(response.occurredAt()).isEqualTo(result.occurredAt());
    }

    @Test
    void from_WhenGivenNullResult_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedResponse.from(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("result cannot be null");
    }

    @Test
    void records_WhenGivenSameValues_ShouldBeEqual() {
        UUID eventId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Instant occurredAt = Instant.parse("2026-05-04T11:30:00Z");
        TimeAdvancedResponse firstResponse = new TimeAdvancedResponse(eventId, 0, 1, 1, occurredAt);
        TimeAdvancedResponse secondResponse = new TimeAdvancedResponse(eventId, 0, 1, 1, occurredAt);

        assertThat(firstResponse).isEqualTo(secondResponse);
        assertThat(firstResponse.hashCode()).isEqualTo(secondResponse.hashCode());
        assertThat(firstResponse).hasToString(secondResponse.toString());
    }
}
