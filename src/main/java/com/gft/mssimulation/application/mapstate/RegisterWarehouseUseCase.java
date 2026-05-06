package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.WarehouseType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisterWarehouseUseCase {

    private final MapStateHolder holder;

    public void execute(UUID warehouseId, String name, Location location, WarehouseType warehouseType){
        holder.get().registerWarehouse(warehouseId, name, location, warehouseType);
    }

}
