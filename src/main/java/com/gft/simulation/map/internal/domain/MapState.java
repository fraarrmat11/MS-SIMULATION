package com.gft.simulation.map.internal.domain;

import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.domain.exceptions.WarehouseAlreadyRegisteredException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MapState {
    List<TruckPosition> trucks = new ArrayList<>();
    List<WarehousePosition> warehouses = new ArrayList<>();

    public void registerTruck(UUID truckId, Location location) {
        if (isTruckAlreadyRegistered(truckId))
            throw new TruckAlreadyRegisteredException(truckId);
        trucks.add(new TruckPosition(truckId, location));
    }

    public void updateTruckPosition(UUID truckId, Location location) {
        TruckPosition existingTruckPosition = findTruckOrThrow(truckId);
        existingTruckPosition.updateLocation(location);
    }

    public void registerWarehouse(UUID warehouseId, String name, Location location, WarehouseType warehouseType) {
        if (isWarehouseAlreadyRegistered(warehouseId))
            throw new WarehouseAlreadyRegisteredException(warehouseId);
        warehouses.add(new WarehousePosition(warehouseId, name, location, warehouseType));
    }

    public List<TruckPosition> getTrucks() {
        return Collections.unmodifiableList(trucks);
    }

    public List<WarehousePosition> getWarehouses() {
        return Collections.unmodifiableList(warehouses);
    }

    private boolean isTruckAlreadyRegistered(UUID truckId) {
        return trucks.stream().anyMatch(t -> t.getTruckId().equals(truckId));
    }

    private boolean isWarehouseAlreadyRegistered(UUID warehouseId) {
        return warehouses.stream().anyMatch(w -> w.getWarehouseId().equals(warehouseId));
    }

    private TruckPosition findTruckOrThrow(UUID truckId) {
        return trucks.stream()
                .filter(t -> t.getTruckId().equals(truckId))
                .findFirst()
                .orElseThrow(() -> new TruckNotFoundException(truckId));
    }

    public static MapState restoreFromPersistence(List<TruckPosition> trucks, List<WarehousePosition> warehouses) {
        MapState mapState = new MapState();
        mapState.trucks.addAll(trucks);
        mapState.warehouses.addAll(warehouses);
        return mapState;
    }

    public void deleteTruck(UUID truckId){
        TruckPosition truckToDelete = findTruckOrThrow(truckId);
        this.trucks.remove(truckToDelete);
    }
}
