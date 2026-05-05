package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.application.port.out.TruckPositionPort;
import com.gft.mssimulation.application.port.out.WarehousePositionPort;
import com.gft.mssimulation.domain.mapstate.MapState;
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