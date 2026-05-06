package com.gft.mssimulation.infrastructure.messaging.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.mssimulation.application.port.out.TimeAdvancedEventPublisher;
import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TimeAdvancedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RabbitTimeAdvancedEventPublisher implements TimeAdvancedEventPublisher {

    private static final String TIME_ADVANCED_ROUTING_KEY = "time.advanced.v1";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitTimeAdvancedEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = Objects.requireNonNull(rabbitTemplate, "rabbitTemplate cannot be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
    }

    @Override
    public void publish(TimeAdvancedEvent event) {
        Objects.requireNonNull(event, "event cannot be null");

        rabbitTemplate.convertAndSend(TIME_ADVANCED_ROUTING_KEY, serialize(TimeAdvancedMessage.from(event)));
    }

    private String serialize(TimeAdvancedMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize time advanced message", exception);
        }
    }
}
