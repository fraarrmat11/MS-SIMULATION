package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.application.mapstate.RegisterWarehouseUseCase;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.WarehouseRegisteredEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@AllArgsConstructor
public class WarehouseRegisteredListener {

    private final RegisterWarehouseUseCase useCase;

    @RabbitListener(queues = "warehouse.registered.v1")
    void onEvent(WarehouseRegisteredEvent event){
        useCase.execute(
                event.getWarehouseId(),
                event.getName(),
                event.getLocation(),
                event.getWarehouseType()
        );
    }
}
