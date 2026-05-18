package com.gft.simulation.map.internal.application.port.out;

import com.gft.simulation.map.internal.domain.WarehousePosition;

import java.util.List;

public interface WarehousePositionPort {
    List<WarehousePosition> findAll();
    void save(WarehousePosition warehousePosition);
}
