package com.gft.simulation.clock.internal.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSimulationClockRepository extends JpaRepository<SimulationClockEntity, Long> {
}
