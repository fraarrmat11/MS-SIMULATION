package com.gft.simulation.clock.internal.application.service.impl;

import com.gft.simulation.clock.internal.application.port.out.SimulationClockRepository;
import com.gft.simulation.clock.internal.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.simulation.clock.internal.domain.SimulationClock;
import com.gft.simulation.clock.internal.domain.SimulationDay;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class GetCurrentSimulationDayServiceImplTest {

    private final SimulationClockRepository simulationClockRepository = mock(SimulationClockRepository.class);

    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase =
            new GetCurrentSimulationDayServiceImpl(simulationClockRepository);

    @Test
    void getCurrentSimulationDay_WhenClockExists_ShouldLoadClockAndReturnCurrentDay() {
        SimulationClock simulationClock = SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(8));
        when(simulationClockRepository.load()).thenReturn(simulationClock);

        int currentDay = getCurrentSimulationDayUseCase.getCurrentSimulationDay();

        assertThat(currentDay).isEqualTo(8);
        verify(simulationClockRepository).load();
        verifyNoMoreInteractions(simulationClockRepository);
    }

    @Test
    void constructor_WhenGivenNullRepository_ShouldThrowException() {
        assertThatThrownBy(() -> new GetCurrentSimulationDayServiceImpl(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("simulationClockRepository cannot be null");
    }
}
