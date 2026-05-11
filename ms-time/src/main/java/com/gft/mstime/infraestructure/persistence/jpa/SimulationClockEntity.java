package com.gft.mstime.infraestructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "simulation_clock")
public class SimulationClockEntity {

    @Id
    private Long id;

    @Column(name = "current_day", nullable = false)
    private int currentDay;

    protected SimulationClockEntity() {
    }

    public SimulationClockEntity(Long id, int currentDay) {
        if (currentDay < 0) {
            throw new IllegalArgumentException("Current day cannot be negative");
        }
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.currentDay = currentDay;
    }

    public Long getId() {
        return id;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void updateCurrentDay(int currentDay) {
        if (currentDay < 0) {
            throw new IllegalArgumentException("Current day cannot be negative");
        }
        this.currentDay = currentDay;
    }
}
