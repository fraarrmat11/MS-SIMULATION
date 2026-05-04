package com.gft.mssimulation.domain.simulationclock;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationDayTest {

    @Test
    void zero_WhenCalled_ShouldCreateInitialSimulationDayAtZero() {
        SimulationDay simulationDay = SimulationDay.zero();

        assertThat(simulationDay.dayNumber()).isZero();
    }

    @Test
    void of_WhenGivenCorrectDayNumber_ShouldCreateSimulationDay() {
        SimulationDay simulationDay = SimulationDay.of(7);

        assertThat(simulationDay.dayNumber()).isEqualTo(7);
    }

    @Test
    void of_WhenGivenNegativeDayNumber_ShouldThrowException() {
        assertThatThrownBy(() -> SimulationDay.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Simulation day cannot be negative");
    }

    @Test
    void advanceBy_WhenGivenPositiveDays_ShouldReturnAdvancedSimulationDay() {
        SimulationDay simulationDay = SimulationDay.of(3);

        SimulationDay advancedDay = simulationDay.advanceBy(4);

        assertThat(advancedDay.dayNumber()).isEqualTo(7);
    }

    @Test
    void advanceBy_WhenGivenPositiveDays_ShouldKeepOriginalSimulationDayUnchanged() {
        SimulationDay simulationDay = SimulationDay.of(3);

        SimulationDay advancedDay = simulationDay.advanceBy(4);

        assertThat(simulationDay.dayNumber()).isEqualTo(3);
        assertThat(advancedDay.dayNumber()).isEqualTo(7);
    }

    @Test
    void advanceBy_WhenGivenZeroDays_ShouldThrowException() {
        SimulationDay simulationDay = SimulationDay.zero();

        assertThatThrownBy(() -> simulationDay.advanceBy(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Days to advance must be greater than zero");
    }

    @Test
    void advanceBy_WhenGivenNegativeDays_ShouldThrowException() {
        SimulationDay simulationDay = SimulationDay.zero();

        assertThatThrownBy(() -> simulationDay.advanceBy(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Days to advance must be greater than zero");
    }
}
