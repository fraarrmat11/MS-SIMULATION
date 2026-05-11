package com.gft.msmap.application.port.out;

import com.gft.msmap.domain.TruckPosition;

import java.util.List;

public interface TruckPositionPort {
    List<TruckPosition> findAll();
    void save(TruckPosition truckPosition);
}
