package com.gft.mssimulation.application.port.out;

import com.gft.mssimulation.domain.mapstate.WarehousePosition;

import java.util.List;

public interface WarehousePositionPort {

    List<WarehousePosition> findAll();

    void save(WarehousePosition warehousePosition);
}
