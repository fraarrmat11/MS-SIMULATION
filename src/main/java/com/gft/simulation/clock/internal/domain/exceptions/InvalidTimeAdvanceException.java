package com.gft.simulation.clock.internal.domain.exceptions;

import com.gft.simulation.clock.internal.domain.SimulationDay;

public class InvalidTimeAdvanceException extends RuntimeException {
    public InvalidTimeAdvanceException(SimulationDay previousDay, SimulationDay currentDay) {
        super("Current day must be after previous day, got: previousDay=" + previousDay.dayNumber() + ", currentDay=" + currentDay.dayNumber());
    }
}
