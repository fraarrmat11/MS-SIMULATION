package com.gft.mssimulation.application.port.out;

import com.gft.mssimulation.domain.mapstate.TruckPosition;

import java.util.List;

public interface TruckPositionPort {
    List<TruckPosition> findAll();
    void save(TruckPosition truckPosition);
}
