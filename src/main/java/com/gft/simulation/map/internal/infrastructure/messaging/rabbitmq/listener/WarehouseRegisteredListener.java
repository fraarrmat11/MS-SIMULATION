package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.RegisterWarehouseUseCase;
import com.gft.simulation.map.internal.domain.exceptions.WarehouseAlreadyRegisteredException;
import com.gft.simulation.map.internal.infrastructure.config.MapRabbitMQConfig;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.WarehouseRegisteredEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class WarehouseRegisteredListener {

    private final RegisterWarehouseUseCase useCase;

    @RabbitListener(queues = MapRabbitMQConfig.WAREHOUSE_REGISTERED_QUEUE)
    public void onEvent(WarehouseRegisteredEvent event) {
        log.debug("Received WarehouseRegistered event: warehouseId={}, type={}",
                event.getWarehouseId(), event.getWarehouseType());
        try {
            useCase.execute(
                    event.getWarehouseId(),
                    event.getName(),
                    event.getLocation(),
                    event.getWarehouseType()
            );
            log.debug("WarehouseRegistered processed successfully: warehouseId={}", event.getWarehouseId());
        } catch (WarehouseAlreadyRegisteredException e) {
            log.warn("Warehouse already registered, ignoring: warehouseId={}", event.getWarehouseId());
        }
    }
}
