package com.gft.simulation.time.internal.application.service.impl;

import com.gft.simulation.time.internal.application.command.AdvanceTimeCommand;
import com.gft.simulation.time.internal.application.port.out.SimulationClockRepository;
import com.gft.simulation.time.internal.application.port.out.TimeAdvancedEventPublisher;
import com.gft.simulation.time.internal.application.result.TimeAdvancedResult;
import com.gft.simulation.time.internal.application.service.AdvanceTimeService;
import com.gft.simulation.time.internal.domain.SimulationClock;
import com.gft.simulation.time.internal.domain.TimeAdvancedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
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

        log.info("Advancing simulation time by {} day(s)", command.days());

        SimulationClock simulationClock = simulationClockRepository.load();
        TimeAdvancedEvent event = simulationClock.advanceDay(command.days());

        simulationClockRepository.save(simulationClock);
        timeAdvancedEventPublisher.publish(event);

        log.info("Simulation time advanced: previousDay={}, currentDay={}, eventId={}",
                event.previousDay().dayNumber(),
                event.currentDay().dayNumber(),
                event.eventId());

        return TimeAdvancedResult.from(event);
    }
}
