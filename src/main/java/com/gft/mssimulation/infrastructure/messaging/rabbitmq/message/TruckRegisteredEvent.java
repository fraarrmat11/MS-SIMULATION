package com.gft.mssimulation.infrastructure.messaging.rabbitmq.message;

import com.gft.mssimulation.domain.mapstate.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TruckRegisteredEvent {
    private UUID truckId;
    private String name;
    private Location location;
    private int capacity;
    private int timestamp;

    public TruckRegisteredEvent(UUID truckId, Location location) {
        this.truckId = truckId;
        this.location = location;
    }
}
