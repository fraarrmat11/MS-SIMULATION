package com.gft.simulation.clock.internal.infrastructure.messaging.rabbitmq;

import com.gft.simulation.clock.internal.application.port.out.TimeAdvancedEventPublisher;
import com.gft.simulation.clock.internal.domain.TimeAdvancedEvent;
import com.gft.simulation.clock.internal.infrastructure.config.ClockRabbitMQConfig;
import com.gft.simulation.clock.internal.infrastructure.messaging.rabbitmq.message.TimeAdvancedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class RabbitTimeAdvancedEventPublisher implements TimeAdvancedEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitTimeAdvancedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = Objects.requireNonNull(rabbitTemplate, "rabbitTemplate cannot be null");
    }

    @Override
    public void publish(TimeAdvancedEvent event) {
        Objects.requireNonNull(event, "event cannot be null");
        TimeAdvancedMessage message = TimeAdvancedMessage.from(event);

        log.debug("Publishing TimeAdvancedEvent: eventId={}, currentDay={}",
                event.eventId(), event.currentDay().dayNumber());
        try {
            rabbitTemplate.convertAndSend(
                    ClockRabbitMQConfig.EXCHANGE,
                    ClockRabbitMQConfig.TIME_ADVANCED_ROUTING_KEY,
                    message
            );
            log.debug("TimeAdvancedEvent published successfully: eventId={}", event.eventId());
        } catch (AmqpException e) {
            log.error("Failed to publish TimeAdvancedEvent: eventId={}", event.eventId(), e);
            throw new IllegalStateException("Error publishing TimeAdvancedEvent", e);
        }
    }
}
