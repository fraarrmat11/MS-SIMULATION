package com.gft.mssimulation.infrastructure.persistence.jpa.mapstate;

import com.gft.mssimulation.domain.mapstate.WarehousePosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WarehousePositionJpaRepository extends JpaRepository<WarehousePositionEntity, UUID> {
}
