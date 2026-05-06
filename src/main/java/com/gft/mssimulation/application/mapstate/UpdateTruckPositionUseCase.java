package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.TruckPosition;
import com.gft.mssimulation.infrastructure.persistence.jpa.mapstate.TruckPositionJpaAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UpdateTruckPositionUseCase {

    private final MapStateHolder holder;
    private final TruckPositionJpaAdapter repository;

    public void execute(UUID truckId, Location location) {
        holder.get().updateTruckPosition(truckId, location);
        repository.save(new TruckPosition(truckId, location));
    }
}