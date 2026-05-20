package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq;

import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WarehouseRegisteredEvent {
    private String warehouseId;
    private String name;
    private Location location;
    private String warehouseType;
}
