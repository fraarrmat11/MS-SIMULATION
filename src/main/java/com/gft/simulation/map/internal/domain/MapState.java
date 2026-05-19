package com.gft.simulation.map.internal.domain;

import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.domain.exceptions.WarehouseAlreadyRegisteredException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapState {
    Map<UUID, TruckPosition> trucks = new ConcurrentHashMap<>();
    Map<UUID, WarehousePosition> warehouses = new ConcurrentHashMap<>();

    public void registerTruck(UUID truckId, Location location) {
        if (trucks.containsKey(truckId))
            throw new TruckAlreadyRegisteredException(truckId);
        trucks.put(truckId, new TruckPosition(truckId, location));
    }

    public void updateTruckPosition(UUID truckId, Location location) {
        TruckPosition existing = trucks.get(truckId);
        if (existing == null) throw new TruckNotFoundException(truckId);
        existing.updateLocation(location);
    }

    public void deleteTruck(UUID truckId) {
        if (trucks.remove(truckId) == null)
            throw new TruckNotFoundException(truckId);
    }

    public void registerWarehouse(UUID warehouseId, String name, Location location, WarehouseType warehouseType) {
        if (warehouses.containsKey(warehouseId))
            throw new WarehouseAlreadyRegisteredException(warehouseId);
        warehouses.put(warehouseId, new WarehousePosition(warehouseId, name, location, warehouseType));
    }

    public List<TruckPosition> getTrucks() {
        return List.copyOf(trucks.values());
    }

    public List<WarehousePosition> getWarehouses() {
        return List.copyOf(warehouses.values());
    }

    public static MapState restoreFromPersistence(List<TruckPosition> trucks, List<WarehousePosition> warehouses) {
        MapState mapState = new MapState();
        trucks.forEach(t -> mapState.trucks.put(t.getTruckId(), t));
        warehouses.forEach(w -> mapState.warehouses.put(w.getWarehouseId(), w));
        return mapState;
    }

}
