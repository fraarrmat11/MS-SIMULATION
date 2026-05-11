package com.gft.msmap.infraestructure.persistence.jpa;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.TruckPosition;
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
        TruckPositionEntity entity = new TruckPositionEntity(
                truckPosition.getTruckId(),
                truckPosition.getLocation().getX(),
                truckPosition.getLocation().getY()
        );
        repository.save(entity);
    }
}