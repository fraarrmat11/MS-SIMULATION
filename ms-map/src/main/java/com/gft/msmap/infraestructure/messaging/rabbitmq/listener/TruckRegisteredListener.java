package com.gft.msmap.infraestructure.messaging.rabbitmq.listener;

import com.gft.msmap.application.usecase.RegisterTruckUseCase;
import com.gft.msmap.infraestructure.config.RabbitMQConfig;
import com.gft.msmap.infraestructure.messaging.rabbitmq.TruckRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TruckRegisteredListener {

    private final RegisterTruckUseCase useCase;

    @RabbitListener(queues = RabbitMQConfig.TRUCK_REGISTERED_QUEUE)
    public void onEvent(TruckRegisteredEvent event) {
        log.debug("Received TruckRegistered event: truckId={}", event.getTruckId());
        useCase.execute(event.getTruckId(), event.getLocation());
        log.debug("TruckRegistered processed successfully: truckId={}", event.getTruckId());
    }
}
