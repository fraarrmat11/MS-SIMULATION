package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateTruckPositionUseCase {

    private final MapStateHolder holder;

    public UpdateTruckPositionUseCase(MapStateHolder holder) {
        this.holder = holder;
    }

    public void execute(UUID truckId, Location location) {
        holder.get().updateTruckPosition(truckId, location);
    }
}