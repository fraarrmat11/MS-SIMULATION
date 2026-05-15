package com.gft.mstime.application.service.impl;

import com.gft.mstime.application.port.out.SimulationClockRepository;
import com.gft.mstime.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.mstime.domain.SimulationClock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
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
        int currentDay = simulationClockRepository.load().getCurrentDay().dayNumber();
        log.debug("Current simulation day queried: day={}", currentDay);
        return currentDay;
    }
}
