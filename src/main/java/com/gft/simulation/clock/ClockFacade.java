package com.gft.simulation.clock;

import com.gft.simulation.clock.internal.application.usecase.GetCurrentSimulationDayUseCase;
import org.springframework.stereotype.Service;

@Service
public class ClockFacade {

    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase;

    public ClockFacade(GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase) {
        this.getCurrentSimulationDayUseCase = getCurrentSimulationDayUseCase;
    }

    public int getCurrentSimulationDay() {
        return getCurrentSimulationDayUseCase.getCurrentSimulationDay();
    }
}
