package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.mapstate.Location;

import java.util.UUID;

public class TruckPositionUpdatedEvent {

    private UUID truckId;
    private Location location;

    public TruckPositionUpdatedEvent(UUID truckId, Location location) {
        this.truckId = truckId;
        this.location = location;
    }

    public UUID getTruckId() { return truckId; }
    public Location getLocation() { return location; }
}
