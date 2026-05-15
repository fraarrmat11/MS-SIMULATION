package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.WarehousePositionPort;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.WarehousePosition;
import com.gft.msmap.domain.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterWarehouseUseCase {

    private final MapStateHolder holder;
    private final WarehousePositionPort warehousePositionPort;

    public void execute(UUID warehouseId, String name, Location location, WarehouseType warehouseType) {
        log.info("Registering warehouse: warehouseId={}, type={}", warehouseId, warehouseType);
        holder.get().registerWarehouse(warehouseId, name, location, warehouseType);
        warehousePositionPort.save(new WarehousePosition(warehouseId, name, location, warehouseType));
        log.info("Warehouse registered successfully: warehouseId={}, type={}", warehouseId, warehouseType);
    }
}
