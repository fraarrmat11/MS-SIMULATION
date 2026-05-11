package com.gft.mstime.infraestructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSimulationClockRepository extends JpaRepository<SimulationClockEntity, Long> {
}
