package com.gft.mstime.domain;

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
        currentDay = currentDay.advanceBy(daysToAdvance);

        return TimeAdvancedEvent.of(previousDay, currentDay);
    }

    public SimulationDay getCurrentDay() {
        return currentDay;
    }
}
