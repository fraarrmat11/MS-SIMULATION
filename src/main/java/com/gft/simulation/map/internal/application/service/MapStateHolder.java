package com.gft.simulation.map.internal.application.service;

import com.gft.simulation.map.internal.domain.MapState;

public interface MapStateHolder {
    MapState get();
    void set(MapState mapState);
}
