package com.gft.simulation.map.internal.domain.exceptions;

import java.util.UUID;

public class WarehouseAlreadyRegisteredException extends RuntimeException {
    public WarehouseAlreadyRegisteredException(String warehouseId) {
        super("Warehouse already registered: " + warehouseId);
    }
}
