package com.gft.mssimulation.infrastructure.messaging.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RabbitTimeAdvancedEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final RabbitTimeAdvancedEventPublisher publisher = new RabbitTimeAdvancedEventPublisher(
            rabbitTemplate,
            objectMapper
    );

    @Test
    void constructor_WhenGivenNullRabbitTemplate_ShouldThrowException() {
        assertThatThrownBy(() -> new RabbitTimeAdvancedEventPublisher(null, objectMapper))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rabbitTemplate cannot be null");
    }

    @Test
    void constructor_WhenGivenNullObjectMapper_ShouldThrowException() {
        assertThatThrownBy(() -> new RabbitTimeAdvancedEventPublisher(rabbitTemplate, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("objectMapper cannot be null");
    }

    @Test
    void publish_WhenGivenTimeAdvancedEvent_ShouldPublishJsonUsingTimeAdvancedRoutingKey() {
        TimeAdvancedEvent event = TimeAdvancedEvent.occurred(2, 5, 3);

        publisher.publish(event);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate).convertAndSend(eq("time.advanced.v1"), messageCaptor.capture());

        assertThat(messageCaptor.getValue())
                .contains("\"eventId\":\"" + event.eventId() + "\"")
                .contains("\"previousDay\":2")
                .contains("\"currentDay\":5")
                .contains("\"daysAdvanced\":3")
                .contains("\"occurredAt\":\"" + event.occurredAt() + "\"");

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
    void publish_WhenMessageCannotBeSerialized_ShouldThrowExceptionAndNotPublish() throws JsonProcessingException {
        ObjectMapper failingObjectMapper = mock(ObjectMapper.class);
        TimeAdvancedEvent event = TimeAdvancedEvent.occurred(2, 5, 3);
        RabbitTimeAdvancedEventPublisher failingPublisher = new RabbitTimeAdvancedEventPublisher(
                rabbitTemplate,
                failingObjectMapper
        );

        when(failingObjectMapper.writeValueAsString(any()))
                .thenThrow(new JsonProcessingException("serialization failed") {
                });

        assertThatThrownBy(() -> failingPublisher.publish(event))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Could not serialize time advanced message")
                .hasCauseInstanceOf(JsonProcessingException.class);

        verifyNoInteractions(rabbitTemplate);
    }
}
