package com.gft.simulation.time.internal.domain;

import com.gft.simulation.time.internal.domain.exceptions.InvalidDaysToAdvanceException;
import com.gft.simulation.time.internal.domain.exceptions.InvalidSimulationDayException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationDayTest {

    @Test
    void dayZero_WhenCalled_ShouldCreateInitialSimulationDayAtZero() {
        SimulationDay simulationDay = SimulationDay.dayZero();

        assertThat(simulationDay.dayNumber()).isZero();
    }

    @Test
    void fromDayNumber_WhenGivenCorrectDayNumber_ShouldCreateSimulationDay() {
        SimulationDay simulationDay = SimulationDay.fromDayNumber(7);

        assertThat(simulationDay.dayNumber()).isEqualTo(7);
    }

    @Test
    void fromDayNumber_WhenGivenNegativeDayNumber_ShouldThrowException() {
        assertThatThrownBy(() -> SimulationDay.fromDayNumber(-1))
                .isInstanceOf(InvalidSimulationDayException.class);
    }

    @Test
    void advanceBy_WhenGivenPositiveDays_ShouldReturnAdvancedSimulationDay() {
        SimulationDay simulationDay = SimulationDay.fromDayNumber(3);

        SimulationDay advancedDay = simulationDay.advanceBy(4);

        assertThat(advancedDay.dayNumber()).isEqualTo(7);
    }

    @Test
    void advanceBy_WhenGivenPositiveDays_ShouldKeepOriginalSimulationDayUnchanged() {
        SimulationDay simulationDay = SimulationDay.fromDayNumber(3);

        SimulationDay advancedDay = simulationDay.advanceBy(4);

        assertThat(simulationDay.dayNumber()).isEqualTo(3);
        assertThat(advancedDay.dayNumber()).isEqualTo(7);
    }

    @Test
    void advanceBy_WhenGivenZeroDays_ShouldThrowException() {
        SimulationDay simulationDay = SimulationDay.dayZero();

        assertThatThrownBy(() -> simulationDay.advanceBy(0))
                .isInstanceOf(InvalidDaysToAdvanceException.class);
    }

    @Test
    void advanceBy_WhenGivenNegativeDays_ShouldThrowException() {
        SimulationDay simulationDay = SimulationDay.dayZero();

        assertThatThrownBy(() -> simulationDay.advanceBy(-2))
                .isInstanceOf(InvalidDaysToAdvanceException.class);
    }

    @Test
    void equals_WhenSameInstance_ShouldBeEqual() {
        SimulationDay day = SimulationDay.fromDayNumber(3);

        assertThat(day.equals(day)).isTrue();
    }

    @Test
    void equals_WhenNull_ShouldNotBeEqual() {
        SimulationDay day = SimulationDay.fromDayNumber(3);

        assertThat(day.equals(null)).isFalse();
    }

    @Test
    void equals_WhenDifferentDayNumber_ShouldNotBeEqual() {
        SimulationDay day1 = SimulationDay.fromDayNumber(3);
        SimulationDay day2 = SimulationDay.fromDayNumber(5);

        assertThat(day1).isNotEqualTo(day2);
    }

    @Test
    void hashCode_WhenSameDayNumber_ShouldReturnSameHash() {
        SimulationDay day1 = SimulationDay.fromDayNumber(3);
        SimulationDay day2 = SimulationDay.fromDayNumber(3);

        assertThat(day1.hashCode()).isEqualTo(day2.hashCode());
    }
}
