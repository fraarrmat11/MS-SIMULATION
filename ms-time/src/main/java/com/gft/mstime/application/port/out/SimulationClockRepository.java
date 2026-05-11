package com.gft.mstime.application.port.out;

import com.gft.mstime.domain.SimulationClock;

public interface SimulationClockRepository {

    SimulationClock load();
    void save(SimulationClock simulationClock);

}
