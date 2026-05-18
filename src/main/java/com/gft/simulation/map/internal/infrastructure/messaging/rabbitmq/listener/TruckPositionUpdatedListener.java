package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.UpdateTruckPositionUseCase;
import com.gft.simulation.map.internal.infrastructure.config.MapRabbitMQConfig;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckPositionUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class TruckPositionUpdatedListener {

    private final UpdateTruckPositionUseCase useCase;

    @RabbitListener(queues = MapRabbitMQConfig.TRUCK_POSITION_UPDATED_QUEUE)
    public void onEvent(TruckPositionUpdatedEvent event) {
        log.debug("Received TruckPositionUpdated event: truckId={}", event.getTruckId());
        try {
            useCase.execute(event.getTruckId(), event.getLocation());
            log.debug("TruckPositionUpdated processed successfully: truckId={}", event.getTruckId());
        } catch (IllegalArgumentException e) {
            log.error("Failed to update position for truckId={}: {}", event.getTruckId(), e.getMessage());
        }
    }
}
