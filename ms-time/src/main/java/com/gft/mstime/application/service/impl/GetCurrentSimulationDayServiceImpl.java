package com.gft.mstime.application.service.impl;

import com.gft.mstime.application.port.out.SimulationClockRepository;
import com.gft.mstime.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.mstime.domain.SimulationClock;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GetCurrentSimulationDayServiceImpl implements GetCurrentSimulationDayUseCase {

    private final SimulationClockRepository simulationClockRepository;

    public GetCurrentSimulationDayServiceImpl(SimulationClockRepository simulationClockRepository) {
        this.simulationClockRepository = Objects.requireNonNull(
                simulationClockRepository,
                "simulationClockRepository cannot be null"
        );
    }

    @Override
    public int getCurrentSimulationDay() {
        SimulationClock simulationClock = simulationClockRepository.load();

        return simulationClock.getCurrentDay().dayNumber();
    }
}
