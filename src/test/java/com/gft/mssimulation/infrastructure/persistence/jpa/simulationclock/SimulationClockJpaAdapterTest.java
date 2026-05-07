package com.gft.mssimulation.infrastructure.persistence.jpa.simulationclock;

import com.gft.mssimulation.domain.simulationclock.SimulationClock;
import com.gft.mssimulation.domain.simulationclock.SimulationDay;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SimulationClockJpaAdapterTest {

    private final SpringDataSimulationClockRepository repository = mock(SpringDataSimulationClockRepository.class);

    private final SimulationClockJpaAdapter adapter = new SimulationClockJpaAdapter(repository);

    @Test
    void constructor_WhenGivenNullRepository_ShouldThrowException() {
        assertThatThrownBy(() -> new SimulationClockJpaAdapter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("repository cannot be null");
    }

    @Test
    void load_WhenClockExists_ShouldReturnPersistedClock() {
        SimulationClockEntity entity = new SimulationClockEntity(1L, 6);
        when(repository.findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID)).thenReturn(Optional.of(entity));

        SimulationClock simulationClock = adapter.load();

        assertThat(simulationClock.getCurrentDay().dayNumber()).isEqualTo(6);
        verify(repository).findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void load_WhenClockDoesNotExist_ShouldReturnInitialClock() {
        when(repository.findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID)).thenReturn(Optional.empty());

        SimulationClock simulationClock = adapter.load();

        assertThat(simulationClock.getCurrentDay().dayNumber()).isZero();
        verify(repository).findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save_WhenClockDoesNotExist_ShouldCreateAndSaveEntityWithCurrentDay() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.of(4));
        when(repository.findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID)).thenReturn(Optional.empty());

        adapter.save(simulationClock);

        ArgumentCaptor<SimulationClockEntity> entityCaptor = ArgumentCaptor.forClass(SimulationClockEntity.class);
        verify(repository).findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID);
        verify(repository).save(entityCaptor.capture());

        SimulationClockEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getId()).isEqualTo(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID);
        assertThat(savedEntity.getCurrentDay()).isEqualTo(4);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save_WhenClockAlreadyExists_ShouldUpdateAndSaveExistingEntity() {
        SimulationClockEntity entity = new SimulationClockEntity(1L, 2);
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.of(9));
        when(repository.findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID)).thenReturn(Optional.of(entity));

        adapter.save(simulationClock);

        verify(repository).findById(SimulationClockJpaAdapter.SIMULATION_CLOCK_ID);
        verify(repository).save(entity);

        assertThat(entity.getCurrentDay()).isEqualTo(9);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void save_WhenGivenNullClock_ShouldThrowExceptionAndNotCallRepository() {
        assertThatThrownBy(() -> adapter.save(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("simulationClock cannot be null");

        verifyNoInteractions(repository);
    }
}
