package com.gft.simulation.clock.internal.infrastructure.messaging.rabbitmq.message;

import com.gft.simulation.clock.internal.domain.SimulationDay;
import com.gft.simulation.clock.internal.domain.TimeAdvancedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeAdvancedMessageTest {

    @Test
    void from_WhenGivenTimeAdvancedEvent_ShouldMapAllFields() {
        TimeAdvancedEvent event = TimeAdvancedEvent.of(SimulationDay.fromDayNumber(0), SimulationDay.fromDayNumber(1));

        TimeAdvancedMessage message = TimeAdvancedMessage.from(event);

        assertThat(message.eventId()).isEqualTo(event.eventId());
        assertThat(message.previousDay()).isEqualTo(event.previousDay().dayNumber());
        assertThat(message.currentDay()).isEqualTo(event.currentDay().dayNumber());
        assertThat(message.daysAdvanced()).isEqualTo(event.daysAdvanced());
        assertThat(message.occurredAt()).isEqualTo(event.occurredAt());
    }

    @Test
    void from_WhenGivenNullEvent_ShouldThrowException() {
        assertThatThrownBy(() -> TimeAdvancedMessage.from(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("event cannot be null");
    }

    @Test
    void records_WhenGivenSameValues_ShouldBeEqual() {
        TimeAdvancedEvent event = TimeAdvancedEvent.of(SimulationDay.fromDayNumber(0), SimulationDay.fromDayNumber(1));

        TimeAdvancedMessage firstMessage = TimeAdvancedMessage.from(event);
        TimeAdvancedMessage secondMessage = TimeAdvancedMessage.from(event);

        assertThat(firstMessage).isEqualTo(secondMessage);
        assertThat(firstMessage.hashCode()).isEqualTo(secondMessage.hashCode());
        assertThat(firstMessage).hasToString(secondMessage.toString());
    }
}
