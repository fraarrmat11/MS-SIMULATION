package com.gft.mstime.application.service.impl;

import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.port.out.SimulationClockRepository;
import com.gft.mstime.application.port.out.TimeAdvancedEventPublisher;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.application.service.AdvanceTimeService;
import com.gft.mstime.domain.SimulationClock;
import com.gft.mstime.domain.TimeAdvancedEvent;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdvanceTimeServiceImpl implements AdvanceTimeService {

    private final SimulationClockRepository simulationClockRepository;
    private final TimeAdvancedEventPublisher timeAdvancedEventPublisher;

    public AdvanceTimeServiceImpl(
            SimulationClockRepository simulationClockRepository,
            TimeAdvancedEventPublisher timeAdvancedEventPublisher
    ) {
        this.simulationClockRepository = Objects.requireNonNull(
                simulationClockRepository,
                "simulationClockRepository cannot be null"
        );
        this.timeAdvancedEventPublisher = Objects.requireNonNull(
                timeAdvancedEventPublisher,
                "timeAdvancedEventPublisher cannot be null"
        );
    }

    @Override
    public TimeAdvancedResult advanceTime(AdvanceTimeCommand command) {
        Objects.requireNonNull(command, "command cannot be null");

        SimulationClock simulationClock = simulationClockRepository.load();

        TimeAdvancedEvent event = simulationClock.advanceDay(command.days());

        simulationClockRepository.save(simulationClock);
        timeAdvancedEventPublisher.publish(event);

        return TimeAdvancedResult.from(event);
    }
}
