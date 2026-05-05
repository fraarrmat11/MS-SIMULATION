package com.gft.mssimulation.infrastructure.persistence.jpa.simulationclock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSimulationClockRepository extends JpaRepository<SimulationClockEntity, Long> {
}
