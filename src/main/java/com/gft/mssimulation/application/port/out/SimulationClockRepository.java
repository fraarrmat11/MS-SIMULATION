package com.gft.mssimulation.application.port.out;

import com.gft.mssimulation.domain.simulationclock.SimulationClock;

public interface SimulationClockRepository {

    SimulationClock load();
    void save(SimulationClock simulationClock);

}
