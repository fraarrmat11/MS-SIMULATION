package com.gft.simulation.map.internal.domain.exceptions;

import java.util.UUID;

public class TruckNotFoundException extends RuntimeException {
    public TruckNotFoundException(UUID truckId) {
        super("Truck not found: " + truckId);
    }
}
