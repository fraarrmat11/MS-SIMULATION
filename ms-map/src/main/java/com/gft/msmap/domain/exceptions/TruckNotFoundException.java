package com.gft.msmap.domain.exceptions;

import java.util.UUID;

public class TruckNotFoundException extends RuntimeException {
    public TruckNotFoundException(UUID truckId) {
        super("Truck not found: " + truckId);
    }
}
