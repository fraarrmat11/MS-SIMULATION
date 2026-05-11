package com.gft.msmap.application.port.out;

import com.gft.msmap.domain.WarehousePosition;

import java.util.List;

public interface WarehousePositionPort {

    List<WarehousePosition> findAll();

    void save(WarehousePosition warehousePosition);
}
