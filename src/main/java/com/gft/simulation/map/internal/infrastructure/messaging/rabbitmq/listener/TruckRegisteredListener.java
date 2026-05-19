package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.RegisterTruckUseCase;
import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.infrastructure.config.MapRabbitMQConfig;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TruckRegisteredListener {

    private final RegisterTruckUseCase useCase;

    @RabbitListener(queues = MapRabbitMQConfig.TRUCK_REGISTERED_QUEUE)
    public void onEvent(TruckRegisteredEvent event) {
        log.debug("Received TruckRegistered event: truckId={}", event.getTruckId());
        try {
            useCase.execute(event.getTruckId(), event.getLocation());
            log.debug("TruckRegistered processed successfully: truckId={}", event.getTruckId());
        } catch (TruckAlreadyRegisteredException e) {
            log.warn("Truck already registered, ignoring: truckId={}", event.getTruckId());
        }
    }
}
