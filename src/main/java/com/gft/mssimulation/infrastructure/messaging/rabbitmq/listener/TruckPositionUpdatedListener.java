package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.application.mapstate.UpdateTruckPositionUseCase;
import com.gft.mssimulation.infrastructure.config.RabbitMQConfig;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TruckPositionUpdatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TruckPositionUpdatedListener {

    private final UpdateTruckPositionUseCase useCase;

    @RabbitListener(queues = RabbitMQConfig.TRUCK_POSITION_UPDATED_QUEUE)
    public void onEvent(TruckPositionUpdatedEvent event) {
        useCase.execute(
                event.getTruckId(),
                event.getLocation()
        );
    }
}
