package com.gft.msmap.infraestructure.messaging.rabbitmq.listener;

import com.gft.msmap.application.usecase.UpdateTruckPositionUseCase;
import com.gft.msmap.infraestructure.config.RabbitMQConfig;
import com.gft.msmap.infraestructure.messaging.rabbitmq.TruckPositionUpdatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TruckPositionUpdatedListener {

    private final UpdateTruckPositionUseCase useCase;

    @RabbitListener(queues = RabbitMQConfig.TRUCK_POSITION_UPDATED_QUEUE)
    public void onEvent(TruckPositionUpdatedEvent event) {
        try{
            useCase.execute(
                    event.getTruckId(),
                    event.getLocation()
            );
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage() + event.getTruckId());
        }
    }
}
