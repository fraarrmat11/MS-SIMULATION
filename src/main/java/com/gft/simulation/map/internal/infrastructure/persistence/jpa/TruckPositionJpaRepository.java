package com.gft.simulation.map.internal.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TruckPositionJpaRepository extends JpaRepository<TruckPositionEntity, UUID> {
}
