package com.gft.msmap.domain.exceptions;

import java.util.UUID;

public class TruckAlreadyRegisteredException extends RuntimeException {
    public TruckAlreadyRegisteredException(UUID truckId) {
        super("Truck already registered: " + truckId);
    }
}
