package com.gft.mssimulation.domain.simulationclock;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeAdvancedEventTest {

    @Test
    void occurred_WhenGivenCorrectArguments_ShouldCreateTimeAdvancedEventWithExpectedPayload() {
        Instant beforeEventCreation = Instant.now();

        TimeAdvancedEvent event = TimeAdvancedEvent.occurred(2, 5, 3);

        Instant afterEventCreation = Instant.now();

        assertThat(event.eventId()).isNotNull();
        assertThat(event.previousDay()).isEqualTo(2);
        assertThat(event.currentDay()).isEqualTo(5);
        assertThat(event.daysAdvanced()).isEqualTo(3);
        assertThat(event.occurredAt())
                .isAfterOrEqualTo(beforeEventCreation)
                .isBeforeOrEqualTo(afterEventCreation);
    }

    @Test
    void occurred_WhenCalledForDifferentEvents_ShouldCreateDifferentEventIds() {
        TimeAdvancedEvent firstEvent = TimeAdvancedEvent.occurred(0, 1, 1);
        TimeAdvancedEvent secondEvent = TimeAdvancedEvent.occurred(1, 2, 1);

        assertThat(firstEvent.eventId()).isNotEqualTo(secondEvent.eventId());
    }

    @Test
    void occurred_WhenGivenNegativePreviousDay_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.occurred(-1, 1, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Previous day cannot be negative");
    }

    @Test
    void occurred_WhenGivenNegativeCurrentDay_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.occurred(0, -1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current day cannot be negative");
    }

    @Test
    void occurred_WhenGivenZeroDaysAdvanced_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.occurred(0, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Days advanced must be greater than zero");
    }

    @Test
    void occurred_WhenGivenNegativeDaysAdvanced_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.occurred(2, 1, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Days advanced must be greater than zero");
    }

    @Test
    void occurred_WhenGivenInconsistentCurrentDay_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.occurred(1, 4, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current day must match previous day plus days advanced");
    }
}
