package com.gft.mstime.infraestructure.messaging.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gft.mstime.domain.TimeAdvancedEvent;
import com.gft.mstime.infraestructure.config.RabbitMQConfig;
import com.gft.mstime.infraestructure.messaging.rabbitmq.message.TimeAdvancedMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RabbitTimeAdvancedEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

    private final RabbitTimeAdvancedEventPublisher publisher = new RabbitTimeAdvancedEventPublisher(
            rabbitTemplate
    );

    @Test
    void constructor_WhenGivenNullRabbitTemplate_ShouldThrowException() {
        assertThatThrownBy(() -> new RabbitTimeAdvancedEventPublisher(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rabbitTemplate cannot be null");
    }

    @Test
    void publish_WhenGivenTimeAdvancedEvent_ShouldSendMessage() {
        TimeAdvancedEvent event = TimeAdvancedEvent.timeAdvanced(2, 5, 3);

        publisher.publish(event);

        ArgumentCaptor<TimeAdvancedMessage> messageCaptor =
                ArgumentCaptor.forClass(TimeAdvancedMessage.class);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.TIME_ADVANCED_ROUTING_KEY),
                messageCaptor.capture()
        );

        TimeAdvancedMessage message = messageCaptor.getValue();

        assertThat(message.eventId()).isEqualTo(event.eventId());
        assertThat(message.currentDay()).isEqualTo(event.currentDay());

        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void publish_WhenGivenNullEvent_ShouldThrowExceptionAndNotPublish() {
        assertThatThrownBy(() -> publisher.publish(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("event cannot be null");

        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void publish_WhenAmqpExceptionOccurs_ShouldThrowIllegalStateException() {
        // Arrange
        TimeAdvancedEvent event = TimeAdvancedEvent.timeAdvanced(2, 5, 3);

        doThrow(new AmqpException("Connection failed"))
                .when(rabbitTemplate)
                .convertAndSend(
                        eq(RabbitMQConfig.EXCHANGE),
                        eq(RabbitMQConfig.TIME_ADVANCED_ROUTING_KEY),
                        any(TimeAdvancedMessage.class)
                );

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> publisher.publish(event)
        );

        assertThat(ex.getMessage()).isEqualTo("Error publishing TimeAdvancedEvent");
        assertThat(ex.getCause()).isInstanceOf(AmqpException.class);
    }

}
