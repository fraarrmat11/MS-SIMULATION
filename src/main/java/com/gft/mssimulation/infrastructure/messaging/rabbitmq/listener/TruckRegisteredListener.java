package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.application.mapstate.MapStateHolder;
import com.gft.mssimulation.application.mapstate.RegisterTruckUseCase;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TruckRegisteredEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TruckRegisteredListener {

    private final RegisterTruckUseCase useCase;

    @RabbitListener(queues = "truck.registered.v1")
    public void onEvent(TruckRegisteredEvent event){
        useCase.execute(
                event.getTruckId(),
                event.getLocation()
        );
    }
}
