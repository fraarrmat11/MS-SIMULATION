package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.WarehousePositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.WarehousePosition;
import com.gft.simulation.map.internal.domain.WarehouseType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class RegisterWarehouseUseCase {

    private final MapStateHolder holder;
    private final WarehousePositionPort warehousePositionPort;

    @Transactional
    public void execute(String warehouseId, String name, Location location, String warehouseType) {
        log.info("Registering warehouse: warehouseId={}, type={}", warehouseId, warehouseType);
        holder.get().registerWarehouse(warehouseId, name, location, warehouseType);
        warehousePositionPort.save(new WarehousePosition(warehouseId, name, location, warehouseType));
        log.info("Warehouse registered successfully: warehouseId={}, type={}", warehouseId, warehouseType);
    }
}
