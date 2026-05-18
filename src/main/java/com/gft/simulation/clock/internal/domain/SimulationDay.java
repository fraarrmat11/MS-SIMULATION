package com.gft.simulation.clock.internal.domain;

import com.gft.simulation.clock.internal.domain.exceptions.InvalidDaysToAdvanceException;
import com.gft.simulation.clock.internal.domain.exceptions.InvalidSimulationDayException;

import java.util.Objects;

public final class SimulationDay {

    private static final int INITIAL_SIMULATION_DAY_NUMBER = 0;

    private final int dayNumber;

    private SimulationDay(int dayNumber) {
        if (dayNumber < INITIAL_SIMULATION_DAY_NUMBER) {
            throw new InvalidSimulationDayException(dayNumber);
        }
        this.dayNumber = dayNumber;
    }

    public static SimulationDay dayZero() {
        return new SimulationDay(INITIAL_SIMULATION_DAY_NUMBER);
    }

    public static SimulationDay fromDayNumber(int dayNumber) {
        return new SimulationDay(dayNumber);
    }

    public SimulationDay advanceBy(int daysToAdvance) {
        if (daysToAdvance <= 0) {
            throw new InvalidDaysToAdvanceException(daysToAdvance);
        }
        return new SimulationDay(dayNumber + daysToAdvance);
    }

    public int dayNumber() {
        return dayNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationDay other)) return false;
        return dayNumber == other.dayNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dayNumber);
    }

    public boolean isBefore(SimulationDay other) {
        return this.dayNumber < other.dayNumber();
    }
}
