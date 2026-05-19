package com.gft.simulation.time.internal.infrastructure.persistence.jpa;

import com.gft.simulation.time.internal.infrastructure.persistence.jpa.exceptions.InvalidCurrentDayException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SimulationClockEntityTest {

    @Test
    void constructor_WhenGivenValidValues_ShouldCreateEntity() {
        SimulationClockEntity entity = new SimulationClockEntity(1L, 5);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getCurrentDay()).isEqualTo(5);
    }

    @Test
    void constructor_WhenCalledByJpa_ShouldCreateEmptyEntity() {
        SimulationClockEntity entity = new SimulationClockEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getCurrentDay()).isZero();
    }

    @Test
    void constructor_WhenGivenNullId_ShouldThrowException() {
        assertThatThrownBy(() -> new SimulationClockEntity(null, 0))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id cannot be null");
    }

    @Test
    void constructor_WhenGivenNegativeCurrentDay_ShouldThrowException() {
        assertThatThrownBy(() -> new SimulationClockEntity(1L, -1))
                .isInstanceOf(InvalidCurrentDayException.class);
    }

    @Test
    void updateCurrentDay_WhenGivenValidCurrentDay_ShouldUpdateCurrentDay() {
        SimulationClockEntity entity = new SimulationClockEntity(1L, 3);

        entity.updateCurrentDay(8);

        assertThat(entity.getCurrentDay()).isEqualTo(8);
    }

    @Test
    void updateCurrentDay_WhenGivenNegativeCurrentDay_ShouldThrowException() {
        SimulationClockEntity entity = new SimulationClockEntity(1L, 3);

        assertThatThrownBy(() -> entity.updateCurrentDay(-1))
                .isInstanceOf(InvalidCurrentDayException.class);

        assertThat(entity.getCurrentDay()).isEqualTo(3);
    }
}
