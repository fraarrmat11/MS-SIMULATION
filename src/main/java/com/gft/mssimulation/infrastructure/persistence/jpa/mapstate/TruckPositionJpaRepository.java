package com.gft.mssimulation.infrastructure.persistence.jpa.mapstate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TruckPositionJpaRepository extends JpaRepository<TruckPositionEntity, UUID> {
}
