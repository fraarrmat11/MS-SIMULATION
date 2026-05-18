package com.gft.simulation.time;

import com.gft.simulation.time.internal.application.usecase.GetCurrentSimulationDayUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TimeFacadeTest {

    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase = mock(GetCurrentSimulationDayUseCase.class);

    private final TimeFacade timeFacade = new TimeFacade(getCurrentSimulationDayUseCase);

    @Test
    void getCurrentSimulationDay_ShouldDelegateToUseCase() {
        when(getCurrentSimulationDayUseCase.getCurrentSimulationDay()).thenReturn(5);

        int result = timeFacade.getCurrentSimulationDay();

        assertThat(result).isEqualTo(5);
        verify(getCurrentSimulationDayUseCase).getCurrentSimulationDay();
        verifyNoMoreInteractions(getCurrentSimulationDayUseCase);
    }
}
