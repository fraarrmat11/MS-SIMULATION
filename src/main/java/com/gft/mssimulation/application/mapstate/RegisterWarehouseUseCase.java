package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.WarehousePosition;
import com.gft.mssimulation.domain.mapstate.WarehouseType;
import com.gft.mssimulation.infrastructure.persistence.jpa.mapstate.WarehousePositionJpaAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisterWarehouseUseCase {

    private final MapStateHolder holder;
    private final WarehousePositionJpaAdapter repository;

    public void execute(UUID warehouseId, String name, Location location, WarehouseType warehouseType){
        holder.get().registerWarehouse(warehouseId, name, location, warehouseType);
        repository.save(new WarehousePosition(warehouseId,name,location,warehouseType));
    }

}
