package com.gft.simulation.clock.internal.application.port.out;

import com.gft.simulation.clock.internal.domain.SimulationClock;

public interface SimulationClockRepository {

    SimulationClock load();
    void save(SimulationClock simulationClock);
}
