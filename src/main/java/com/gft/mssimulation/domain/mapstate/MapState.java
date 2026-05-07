package com.gft.mssimulation.domain.mapstate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MapState {
    List<TruckPosition> trucks = new ArrayList<>();
    List<WarehousePosition> warehouses = new ArrayList<>();

    public void registerTruck(UUID truckId, Location location){
        if (this.trucks.stream().anyMatch(truckPosition -> truckPosition.getTruckId().equals(truckId))){
            throw new IllegalArgumentException("Truck is already registered");
        }
        trucks.add(new TruckPosition(truckId, location));
    }

    public void updateTruckPosition(UUID truckId, Location location){
        TruckPosition truckPosition = trucks.stream().filter
                (t -> t.getTruckId().equals(truckId)).
                findFirst().orElseThrow(() -> new IllegalArgumentException("truck not found"));
        trucks.remove(truckPosition);
        registerTruck(truckId, location);
    }

    public void registerWarehouse(UUID warehouseId, String name, Location location, WarehouseType warehouseType){
        warehouses.add(new WarehousePosition(warehouseId, name, location, warehouseType));
    }
}
