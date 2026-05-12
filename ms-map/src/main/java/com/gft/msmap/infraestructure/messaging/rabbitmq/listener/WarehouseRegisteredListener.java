package com.gft.msmap.infraestructure.messaging.rabbitmq.listener;

import com.gft.msmap.application.usecase.RegisterWarehouseUseCase;
import com.gft.msmap.infraestructure.config.RabbitMQConfig;
import com.gft.msmap.infraestructure.messaging.rabbitmq.WarehouseRegisteredEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WarehouseRegisteredListener {

    private final RegisterWarehouseUseCase useCase;

    @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_REGISTERED_QUEUE)
    public void onEvent(WarehouseRegisteredEvent event) {
        try {
            useCase.execute(
                    event.getWarehouseId(),
                    event.getName(),
                    event.getLocation(),
                    event.getWarehouseType()
            );
        } catch (Exception e) {
            throw e;
        }
    }
}
