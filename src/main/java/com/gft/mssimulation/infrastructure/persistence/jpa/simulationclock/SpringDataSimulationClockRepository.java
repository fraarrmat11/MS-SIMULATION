package com.gft.mssimulation.infrastructure.persistence.jpa.simulationclock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSimulationClockRepository extends JpaRepository<SimulationClockEntity, Long> {
}
