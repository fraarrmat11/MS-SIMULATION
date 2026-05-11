package com.gft.msmap.infraestructure.messaging.rabbitmq;

import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WarehouseRegisteredEvent {
    private UUID warehouseId;
    private String name;
    private Location location;
    private WarehouseType warehouseType;
}
