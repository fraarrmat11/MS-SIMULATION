package com.gft.simulation.time;

import com.gft.simulation.time.internal.application.usecase.GetCurrentSimulationDayUseCase;
import org.springframework.stereotype.Service;

@Service
public class TimeFacade {

    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase;

    public TimeFacade(GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase) {
        this.getCurrentSimulationDayUseCase = getCurrentSimulationDayUseCase;
    }

    public int getCurrentSimulationDay() {
        return getCurrentSimulationDayUseCase.getCurrentSimulationDay();
    }
}
