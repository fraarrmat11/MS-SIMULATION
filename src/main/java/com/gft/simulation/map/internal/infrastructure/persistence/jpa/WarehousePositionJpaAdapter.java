package com.gft.simulation.map.internal.infrastructure.persistence.jpa;

import com.gft.simulation.map.internal.application.port.out.WarehousePositionPort;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.WarehousePosition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehousePositionJpaAdapter implements WarehousePositionPort {

    private final WarehousePositionJpaRepository repository;

    public WarehousePositionJpaAdapter(WarehousePositionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<WarehousePosition> findAll() {
        return repository.findAll().stream()
                .map(e -> new WarehousePosition(
                        e.getWarehouseId(),
                        e.getName(),
                        new Location(e.getXEdge(), e.getYEdge()),
                        e.getWarehouseType()
                ))
                .toList();
    }

    @Override
    public void save(WarehousePosition warehousePosition) {
        WarehousePositionEntity entity = new WarehousePositionEntity();
        entity.setWarehouseId(warehousePosition.getWarehouseId());
        entity.setName(warehousePosition.getName());
        entity.setXEdge(warehousePosition.getLocation().getX());
        entity.setYEdge(warehousePosition.getLocation().getY());
        entity.setWarehouseType(warehousePosition.getType());
        repository.save(entity);
    }
}
