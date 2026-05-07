package com.gft.mssimulation.domain.simulationclock;

import java.util.Objects;

public final class SimulationClock {

    private SimulationDay currentDay;

    private SimulationClock(SimulationDay currentDay) {
        this.currentDay = Objects.requireNonNull(currentDay, "currentDay cannot be null");
    }

    public static SimulationClock startingAtDayZero() {
        return new SimulationClock(SimulationDay.dayZero());
    }

    public static SimulationClock fromCurrentDay(SimulationDay currentDay) {
        return new SimulationClock(currentDay);
    }

    public TimeAdvancedEvent advanceDay(int daysToAdvance) {
        SimulationDay previousDay = currentDay;
        SimulationDay advancedDay = currentDay.advanceBy(daysToAdvance);

        currentDay = advancedDay;

        return TimeAdvancedEvent.occurred(
                previousDay.dayNumber(),
                advancedDay.dayNumber(),
                daysToAdvance
        );
    }

    public SimulationDay getCurrentDay() {
        return currentDay;
    }
}
