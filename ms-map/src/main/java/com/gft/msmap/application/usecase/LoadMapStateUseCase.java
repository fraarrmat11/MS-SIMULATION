package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.application.port.out.WarehousePositionPort;
import com.gft.msmap.domain.MapState;
import org.springframework.stereotype.Service;

@Service
public class LoadMapStateUseCase {

    private final TruckPositionPort truckPort;
    private final WarehousePositionPort warehousePort;

    public LoadMapStateUseCase(TruckPositionPort truckPort,
                               WarehousePositionPort warehousePort) {
        this.truckPort = truckPort;
        this.warehousePort = warehousePort;
    }

    public MapState execute() {

        MapState mapState = new MapState();

        truckPort.findAll().forEach(t ->
                mapState.registerTruck(
                        t.getTruckId(),
                        t.getLocation()
                )
        );

        warehousePort.findAll().forEach(w ->
                mapState.registerWarehouse(
                        w.getWarehouseId(),
                        w.getName(),
                        w.getLocation(),
                        w.getType()
                )
        );

        return mapState;
    }
}