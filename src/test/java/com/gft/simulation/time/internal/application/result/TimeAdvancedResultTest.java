package com.gft.simulation.time.internal.application.result;

import com.gft.simulation.time.internal.domain.SimulationDay;
import com.gft.simulation.time.internal.domain.TimeAdvancedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeAdvancedResultTest {

    @Test
    void from_WhenGivenTimeAdvancedEvent_ShouldMapAllFields() {
        SimulationDay previousDay = SimulationDay.fromDayNumber(2);
        SimulationDay currentDay = SimulationDay.fromDayNumber(5);

        TimeAdvancedEvent event = TimeAdvancedEvent.of(previousDay, currentDay);

        TimeAdvancedResult result = TimeAdvancedResult.from(event);

        assertThat(result.eventId()).isEqualTo(event.eventId());
        assertThat(result.previousDay()).isEqualTo(event.previousDay().dayNumber());
        assertThat(result.currentDay()).isEqualTo(event.currentDay().dayNumber());
        assertThat(result.daysAdvanced()).isEqualTo(event.daysAdvanced());
        assertThat(result.occurredAt()).isEqualTo(event.occurredAt());
    }

    @Test
    void records_WhenGivenSameValues_ShouldBeEqual() {
        TimeAdvancedEvent event = TimeAdvancedEvent.of(SimulationDay.fromDayNumber(0), SimulationDay.fromDayNumber(1));

        TimeAdvancedResult firstResult = TimeAdvancedResult.from(event);
        TimeAdvancedResult secondResult = TimeAdvancedResult.from(event);

        assertThat(firstResult).isEqualTo(secondResult);
        assertThat(firstResult.hashCode()).isEqualTo(secondResult.hashCode());
        assertThat(firstResult).hasToString(secondResult.toString());
    }
}
