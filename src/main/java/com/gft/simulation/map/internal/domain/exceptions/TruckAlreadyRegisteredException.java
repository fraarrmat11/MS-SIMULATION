package com.gft.simulation.map.internal.domain.exceptions;

import java.util.UUID;

public class TruckAlreadyRegisteredException extends RuntimeException {
    public TruckAlreadyRegisteredException(UUID truckId) {
        super("Truck already registered: " + truckId);
    }
}
