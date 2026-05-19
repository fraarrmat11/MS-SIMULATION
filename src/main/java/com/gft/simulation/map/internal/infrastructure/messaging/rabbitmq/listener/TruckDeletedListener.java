package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.DeleteTruckUseCase;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.infrastructure.config.MapRabbitMQConfig;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckDeletedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class TruckDeletedListener {

    private final DeleteTruckUseCase deleteTruckUseCase;

    @RabbitListener(queues = MapRabbitMQConfig.TRUCK_DELETED_QUEUE)
    public void onEvent(TruckDeletedEvent event){
        log.debug("Received TruckDeleted event: truckId={}", event.getTruckId());
        try{
            deleteTruckUseCase.execute(event.getTruckId());
            log.debug("TruckDeleted processed successfully: truckId={}", event.getTruckId());
        } catch (TruckNotFoundException e) {
            log.warn("Truck not found, ignoring: truckId={}", event.getTruckId());
        }
    }
}
