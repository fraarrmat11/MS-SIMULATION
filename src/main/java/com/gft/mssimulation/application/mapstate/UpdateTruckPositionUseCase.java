package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapStateHolder;

public class UpdateTruckPositionUseCase {

    private final MapStateHolder holder;

    public UpdateTruckPositionUseCase(MapStateHolder holder) {
        this.holder = holder;
    }

    public void execute(java.util.UUID truckId, Location location) {
        holder.get().updateTruckPosition(truckId, location);
    }
}