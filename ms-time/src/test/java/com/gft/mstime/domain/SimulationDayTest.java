package com.gft.mstime.domain;

import com.gft.mstime.domain.exceptions.InvalidDaysToAdvanceException;
import com.gft.mstime.domain.exceptions.InvalidSimulationDayException;
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
}
