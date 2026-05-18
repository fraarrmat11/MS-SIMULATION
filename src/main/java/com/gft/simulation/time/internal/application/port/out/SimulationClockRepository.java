package com.gft.simulation.time.internal.application.port.out;

import com.gft.simulation.time.internal.domain.SimulationClock;

public interface SimulationClockRepository {

    SimulationClock load();
    void save(SimulationClock simulationClock);
}
