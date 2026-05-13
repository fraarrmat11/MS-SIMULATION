package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.application.port.out.WarehousePositionPort;
import com.gft.msmap.domain.MapState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoadMapStateUseCase {

    private final TruckPositionPort truckPort;
    private final WarehousePositionPort warehousePort;

    public MapState execute() {
        return MapState.restoreFromPersistence(
                truckPort.findAll(),
                warehousePort.findAll()
        );
    }
}