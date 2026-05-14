package com.gft.mstime.domain.exceptions;

import com.gft.mstime.domain.SimulationDay;

public class InvalidTimeAdvanceException extends RuntimeException {
    public InvalidTimeAdvanceException(SimulationDay previousDay, SimulationDay currentDay) {
        super("Current day must be after previous day, got: previousDay=" + previousDay.dayNumber() + ", currentDay=" + currentDay.dayNumber());
    }
}
