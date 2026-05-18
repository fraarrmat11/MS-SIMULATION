package com.gft.simulation.map.internal.application.port.out;

import com.gft.simulation.map.internal.domain.TruckPosition;

import java.util.List;
import java.util.UUID;

public interface TruckPositionPort {
    List<TruckPosition> findAll();
    void save(TruckPosition truckPosition);
    void delete(UUID truckId);
}
