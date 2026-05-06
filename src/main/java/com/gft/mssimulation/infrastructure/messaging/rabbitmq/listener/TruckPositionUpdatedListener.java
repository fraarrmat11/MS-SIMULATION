package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.application.mapstate.MapStateHolder;
import com.gft.mssimulation.application.mapstate.UpdateTruckPositionUseCase;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TruckPositionUpdatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TruckPositionUpdatedListener {

    private final UpdateTruckPositionUseCase useCase;

    @RabbitListener(queues = "truck.position.updated.v1")
    public void onEvent(TruckPositionUpdatedEvent event) {
        useCase.execute(
                event.getTruckId(),
                event.getLocation()
        );
    }
}