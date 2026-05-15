package com.gft.msmap.infraestructure.messaging.rabbitmq.listener;

import com.gft.msmap.application.usecase.RegisterWarehouseUseCase;
import com.gft.msmap.infraestructure.config.RabbitMQConfig;
import com.gft.msmap.infraestructure.messaging.rabbitmq.WarehouseRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class WarehouseRegisteredListener {

    private final RegisterWarehouseUseCase useCase;

    @RabbitListener(queues = RabbitMQConfig.WAREHOUSE_REGISTERED_QUEUE)
    public void onEvent(WarehouseRegisteredEvent event) {
        log.debug("Received WarehouseRegistered event: warehouseId={}, type={}",
                event.getWarehouseId(), event.getWarehouseType());
        useCase.execute(
                event.getWarehouseId(),
                event.getName(),
                event.getLocation(),
                event.getWarehouseType()
        );
        log.debug("WarehouseRegistered processed successfully: warehouseId={}", event.getWarehouseId());
    }
}
