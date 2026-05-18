package com.gft.simulation.map.internal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TruckPosition {
    private UUID truckId;
    private Location location;

    public void updateLocation(Location newLocation) {
        this.location = newLocation;
    }
}
