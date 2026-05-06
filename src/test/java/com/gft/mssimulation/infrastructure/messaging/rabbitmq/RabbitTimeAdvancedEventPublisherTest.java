package com.gft.mssimulation.infrastructure.messaging.rabbitmq;

import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TimeAdvancedMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class RabbitTimeAdvancedEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

    private final RabbitTimeAdvancedEventPublisher publisher = new RabbitTimeAdvancedEventPublisher(rabbitTemplate);

    @Test
    void constructor_WhenGivenNullRabbitTemplate_ShouldThrowException() {
        assertThatThrownBy(() -> new RabbitTimeAdvancedEventPublisher(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rabbitTemplate cannot be null");
    }

    @Test
    void publish_WhenGivenTimeAdvancedEvent_ShouldPublishMessageUsingTimeAdvancedRoutingKey() {
        TimeAdvancedEvent event = TimeAdvancedEvent.occurred(2, 5, 3);

        publisher.publish(event);

        ArgumentCaptor<TimeAdvancedMessage> messageCaptor = ArgumentCaptor.forClass(TimeAdvancedMessage.class);
        verify(rabbitTemplate).convertAndSend(eq("time.advanced.v1"), messageCaptor.capture());

        TimeAdvancedMessage message = messageCaptor.getValue();
        assertThat(message.eventId()).isEqualTo(event.eventId());
        assertThat(message.previousDay()).isEqualTo(event.previousDay());
        assertThat(message.currentDay()).isEqualTo(event.currentDay());
        assertThat(message.daysAdvanced()).isEqualTo(event.daysAdvanced());
        assertThat(message.occurredAt()).isEqualTo(event.occurredAt());

        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void publish_WhenGivenNullEvent_ShouldThrowExceptionAndNotPublish() {
        assertThatThrownBy(() -> publisher.publish(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("event cannot be null");

        verifyNoInteractions(rabbitTemplate);
    }
}
