package com.gft.mssimulation.application.simulationclock;

import com.gft.mssimulation.application.port.out.SimulationClockRepository;
import com.gft.mssimulation.application.port.out.TimeAdvancedEventPublisher;
import com.gft.mssimulation.application.simulationclock.command.AdvanceTimeCommand;
import com.gft.mssimulation.application.simulationclock.result.TimeAdvancedResult;
import com.gft.mssimulation.domain.simulationclock.SimulationClock;
import com.gft.mssimulation.domain.simulationclock.TimeAdvancedEvent;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdvanceTimeService implements AdvanceTimeUseCase {

    private final SimulationClockRepository simulationClockRepository;
    private final TimeAdvancedEventPublisher timeAdvancedEventPublisher;

    public AdvanceTimeService(
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