package com.gft.mssimulation.infrastructure.persistence.jpa.mapstate;

import com.gft.mssimulation.application.port.out.TruckPositionPort;
import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.TruckPosition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TruckPositionJpaAdapter implements TruckPositionPort {

    private final TruckPositionJpaRepository repository;

    public TruckPositionJpaAdapter(TruckPositionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TruckPosition> findAll() {
        return repository.findAll().stream()
                .map(e -> new TruckPosition(
                        e.getTruckId(),
                        new Location(e.getXEdge(), e.getYEdge())
                ))
                .toList();
    }

    @Override
    public void save(TruckPosition truckPosition) {
        TruckPositionEntity entity = new TruckPositionEntity();
        repository.save(entity);
    }
}