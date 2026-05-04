package com.gft.mssimulation.domain.simulationclock;

public final class SimulationDay {

    private static final int INITIAL_SIMULATION_DAY_NUMBER = 0;

    private final int dayNumber;

    private SimulationDay(int dayNumber) {
        if (dayNumber < INITIAL_SIMULATION_DAY_NUMBER) {
            throw new IllegalArgumentException("Simulation day cannot be negative");
        }
        this.dayNumber = dayNumber;
    }

    public static SimulationDay zero() {
        return new SimulationDay(INITIAL_SIMULATION_DAY_NUMBER);
    }

    public static SimulationDay of(int dayNumber) {
        return new SimulationDay(dayNumber);
    }

    public SimulationDay advanceBy(int daysToAdvance) {
        if (daysToAdvance <= 0) {
            throw new IllegalArgumentException("Days to advance must be greater than zero");
        }
        return new SimulationDay(dayNumber + daysToAdvance);
    }

    public int dayNumber() {
        return dayNumber;
    }
}
