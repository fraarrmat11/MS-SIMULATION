package com.gft.simulation.clock;

import com.gft.simulation.clock.internal.application.usecase.GetCurrentSimulationDayUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ClockFacadeTest {

    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase = mock(GetCurrentSimulationDayUseCase.class);

    private final ClockFacade clockFacade = new ClockFacade(getCurrentSimulationDayUseCase);

    @Test
    void getCurrentSimulationDay_ShouldDelegateToUseCase() {
        when(getCurrentSimulationDayUseCase.getCurrentSimulationDay()).thenReturn(5);

        int result = clockFacade.getCurrentSimulationDay();

        assertThat(result).isEqualTo(5);
        verify(getCurrentSimulationDayUseCase).getCurrentSimulationDay();
        verifyNoMoreInteractions(getCurrentSimulationDayUseCase);
    }
}
