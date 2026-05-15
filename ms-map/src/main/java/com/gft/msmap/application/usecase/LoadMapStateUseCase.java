package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.application.port.out.WarehousePositionPort;
import com.gft.msmap.domain.MapState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LoadMapStateUseCase {

    private final TruckPositionPort truckPort;
    private final WarehousePositionPort warehousePort;

    public MapState execute() {
        log.info("Loading map state from persistence");
        MapState mapState = MapState.restoreFromPersistence(
                truckPort.findAll(),
                warehousePort.findAll()
        );
        log.info("Map state loaded: trucks={}, warehouses={}",
                mapState.getTrucks().size(),
                mapState.getWarehouses().size());
        return mapState;
    }
}
