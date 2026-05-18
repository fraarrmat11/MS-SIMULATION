package com.gft.simulation.time.internal.domain;

import com.gft.simulation.time.internal.domain.exceptions.InvalidDaysToAdvanceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationClockTest {

    @Test
    void startingAtDayZero_WhenCalled_ShouldCreateClockAtDayZero() {
        SimulationClock simulationClock = SimulationClock.startingAtDayZero();

        assertThat(simulationClock.getCurrentDay().dayNumber()).isZero();
    }

    @Test
    void fromCurrentDay_WhenGivenCurrentSimulationDay_ShouldCreateClock() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(8));

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(8);
    }

    @Test
    void fromCurrentDay_WhenGivenNullCurrentDay_ShouldThrowException() {
        assertThatThrownBy(() -> SimulationClock.fromCurrentDay(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("currentDay cannot be null");
    }

    @Test
    void advanceDay_WhenGivenOneDay_ShouldUpdateCurrentDayAndReturnTimeAdvancedEvent() {
        SimulationClock simulationClock = SimulationClock.startingAtDayZero();

        TimeAdvancedEvent event = simulationClock.advanceDay(1);

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(1);
        assertThat(event.previousDay().dayNumber()).isZero();
        assertThat(event.currentDay().dayNumber()).isEqualTo(1);
        assertThat(event.daysAdvanced()).isEqualTo(1);
        assertThat(event.eventId()).isNotNull();
        assertThat(event.occurredAt()).isNotNull();
    }

    @Test
    void advanceDay_WhenGivenSeveralDays_ShouldUpdateCurrentDayAndReturnTimeAdvancedEvent() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(5));

        TimeAdvancedEvent event = simulationClock.advanceDay(3);

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(8);
        assertThat(event.previousDay().dayNumber()).isEqualTo(5);
        assertThat(event.currentDay().dayNumber()).isEqualTo(8);
        assertThat(event.daysAdvanced()).isEqualTo(3);
    }

    @Test
    void advanceDay_WhenCalledMultipleTimes_ShouldAdvanceFromLatestCurrentDay() {
        SimulationClock simulationClock = SimulationClock.startingAtDayZero();

        TimeAdvancedEvent firstEvent = simulationClock.advanceDay(2);
        TimeAdvancedEvent secondEvent = simulationClock.advanceDay(3);

        assertThat(firstEvent.previousDay().dayNumber()).isZero();
        assertThat(firstEvent.currentDay().dayNumber()).isEqualTo(2);
        assertThat(secondEvent.previousDay().dayNumber()).isEqualTo(2);
        assertThat(secondEvent.currentDay().dayNumber()).isEqualTo(5);
        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(5);
    }

    @Test
    void advanceDay_WhenGivenInvalidDays_ShouldThrowExceptionWithoutChangingCurrentDay() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(4));

        assertThatThrownBy(() -> simulationClock.advanceDay(0))
                .isInstanceOf(InvalidDaysToAdvanceException.class);

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(4);
    }
}
