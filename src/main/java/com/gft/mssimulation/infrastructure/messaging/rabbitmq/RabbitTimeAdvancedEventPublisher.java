package com.gft.mssimulation.infrastructure.messaging.rabbitmq;

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

    public RabbitTimeAdvancedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = Objects.requireNonNull(rabbitTemplate, "rabbitTemplate cannot be null");
    }

    @Override
    public void publish(TimeAdvancedEvent event) {
        Objects.requireNonNull(event, "event cannot be null");

        rabbitTemplate.convertAndSend(
                TIME_ADVANCED_ROUTING_KEY,
                TimeAdvancedMessage.from(event)
        );
    }
}
