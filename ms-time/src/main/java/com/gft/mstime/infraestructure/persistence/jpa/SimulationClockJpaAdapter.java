package com.gft.mstime.infraestructure.persistence.jpa;

import com.gft.mstime.application.port.out.SimulationClockRepository;
import com.gft.mstime.domain.SimulationClock;
import com.gft.mstime.domain.SimulationDay;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class SimulationClockJpaAdapter implements SimulationClockRepository {

    static final Long SIMULATION_CLOCK_ID = 1L;

    private final SpringDataSimulationClockRepository repository;

    public SimulationClockJpaAdapter(SpringDataSimulationClockRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository cannot be null");
    }

    @Override
    public SimulationClock load() {
        return repository.findById(SIMULATION_CLOCK_ID)
                .map(entity -> SimulationClock.fromCurrentDay(SimulationDay.fromDayNumber(entity.getCurrentDay())))
                .orElseGet(SimulationClock::startingAtDayZero);
    }

    @Override
    public void save(SimulationClock simulationClock) {
        Objects.requireNonNull(simulationClock, "simulationClock cannot be null");

        int currentDay = simulationClock.getCurrentDay().dayNumber();
        SimulationClockEntity entity = repository.findById(SIMULATION_CLOCK_ID)
                .orElseGet(() -> new SimulationClockEntity(SIMULATION_CLOCK_ID, currentDay));

        entity.updateCurrentDay(currentDay);
        repository.save(entity);
    }
}
