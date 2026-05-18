package com.gft.simulation.time.internal.domain.exceptions;

public class InvalidSimulationDayException extends RuntimeException {
    public InvalidSimulationDayException(int dayNumber) {
        super("Simulation day cannot be negative, got: " + dayNumber);
    }
}
