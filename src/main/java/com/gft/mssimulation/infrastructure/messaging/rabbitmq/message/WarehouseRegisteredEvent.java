package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class WarehouseRegisteredEvent {
    UUID warehouseId;
    String name;
    Location location;
    WarehouseType warehouseType;
}
