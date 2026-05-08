package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeAdvancedMessageTest {

    @Test
    void from_WhenGivenTimeAdvancedEvent_ShouldMapAllFields() {
        TimeAdvancedEvent event = TimeAdvancedEvent.timeAdvanced(0, 1, 1);

        TimeAdvancedMessage message = TimeAdvancedMessage.from(event);

        assertThat(message.eventId()).isEqualTo(event.eventId());
        assertThat(message.previousDay()).isEqualTo(event.previousDay());
        assertThat(message.currentDay()).isEqualTo(event.currentDay());
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
        TimeAdvancedEvent event = TimeAdvancedEvent.timeAdvanced(0, 1, 1);

        TimeAdvancedMessage firstMessage = TimeAdvancedMessage.from(event);
        TimeAdvancedMessage secondMessage = TimeAdvancedMessage.from(event);

        assertThat(firstMessage).isEqualTo(secondMessage);
        assertThat(firstMessage.hashCode()).isEqualTo(secondMessage.hashCode());
        assertThat(firstMessage).hasToString(secondMessage.toString());
    }
}
