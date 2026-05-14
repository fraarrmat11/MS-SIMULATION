package com.gft.mstime.domain;

import com.gft.mstime.domain.exceptions.InvalidTimeAdvanceException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeAdvancedEventTest {

    @Test
    void occurred_WhenGivenCorrectArguments_ShouldCreateTimeAdvancedEventWithExpectedPayload() {
        SimulationDay previousDay = SimulationDay.fromDayNumber(2);
        SimulationDay currentDay = SimulationDay.fromDayNumber(5);
        Instant beforeEventCreation = Instant.now();

        TimeAdvancedEvent event = TimeAdvancedEvent.of(previousDay, currentDay);

        Instant afterEventCreation = Instant.now();

        assertThat(event.eventId()).isNotNull();
        assertThat(event.previousDay()).isEqualTo(SimulationDay.fromDayNumber(2));
        assertThat(event.currentDay()).isEqualTo(SimulationDay.fromDayNumber(5));
        assertThat(event.daysAdvanced()).isEqualTo(3);
        assertThat(event.occurredAt())
                .isAfterOrEqualTo(beforeEventCreation)
                .isBeforeOrEqualTo(afterEventCreation);
    }

    @Test
    void occurred_WhenCalledForDifferentEvents_ShouldCreateDifferentEventIds() {
        SimulationDay day0 = SimulationDay.fromDayNumber(0);
        SimulationDay day1 = SimulationDay.fromDayNumber(1);
        SimulationDay day2 = SimulationDay.fromDayNumber(2);

        TimeAdvancedEvent firstEvent = TimeAdvancedEvent.of(day0, day2);
        TimeAdvancedEvent secondEvent = TimeAdvancedEvent.of(day1, day2);

        assertThat(firstEvent.eventId()).isNotEqualTo(secondEvent.eventId());
    }

    @Test
    void of_WhenCurrentDayEqualsPreviousDay_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.of(
                SimulationDay.fromDayNumber(5), SimulationDay.fromDayNumber(5)
        )).isInstanceOf(InvalidTimeAdvanceException.class);
    }

    @Test
    void of_WhenCurrentDayBeforePreviousDay_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedEvent.of(
                SimulationDay.fromDayNumber(5), SimulationDay.fromDayNumber(2)
        )).isInstanceOf(InvalidTimeAdvanceException.class);
    }

    @Test
    void of_WhenSameInstance_ShouldBeEqual() {
        TimeAdvancedEvent event = TimeAdvancedEvent.of(
                SimulationDay.fromDayNumber(0), SimulationDay.fromDayNumber(1)
        );
        assertThat(event).isEqualTo(event);
    }

    @Test
    void equals_WhenDifferentInstances_ShouldNotBeEqual() {
        TimeAdvancedEvent first = TimeAdvancedEvent.of(
                SimulationDay.fromDayNumber(0), SimulationDay.fromDayNumber(1)
        );
        TimeAdvancedEvent second = TimeAdvancedEvent.of(
                SimulationDay.fromDayNumber(0), SimulationDay.fromDayNumber(1)
        );
        assertThat(first).isNotEqualTo(second);
    }

}
