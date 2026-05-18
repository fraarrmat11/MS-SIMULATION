package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.port.out.WarehousePositionPort;
import com.gft.simulation.map.internal.domain.MapState;
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
