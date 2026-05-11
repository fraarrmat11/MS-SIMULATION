package com.gft.mstime.application.result;

import com.gft.mstime.domain.TimeAdvancedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeAdvancedResultTest {

    @Test
    void from_WhenGivenTimeAdvancedEvent_ShouldMapAllFields() {
        TimeAdvancedEvent event = TimeAdvancedEvent.timeAdvanced(2, 5, 3);

        TimeAdvancedResult result = TimeAdvancedResult.from(event);

        assertThat(result.eventId()).isEqualTo(event.eventId());
        assertThat(result.previousDay()).isEqualTo(event.previousDay());
        assertThat(result.currentDay()).isEqualTo(event.currentDay());
        assertThat(result.daysAdvanced()).isEqualTo(event.daysAdvanced());
        assertThat(result.occurredAt()).isEqualTo(event.occurredAt());
    }

    @Test
    void records_WhenGivenSameValues_ShouldBeEqual() {
        TimeAdvancedEvent event = TimeAdvancedEvent.timeAdvanced(0, 1, 1);

        TimeAdvancedResult firstResult = TimeAdvancedResult.from(event);
        TimeAdvancedResult secondResult = TimeAdvancedResult.from(event);

        assertThat(firstResult).isEqualTo(secondResult);
        assertThat(firstResult.hashCode()).isEqualTo(secondResult.hashCode());
        assertThat(firstResult).hasToString(secondResult.toString());
    }
}
