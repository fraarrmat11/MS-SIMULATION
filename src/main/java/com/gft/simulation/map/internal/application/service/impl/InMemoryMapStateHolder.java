package com.gft.simulation.map.internal.application.service.impl;

import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.domain.MapState;
import org.springframework.stereotype.Service;

@Service
public class InMemoryMapStateHolder implements MapStateHolder {

    private volatile MapState mapState;

    @Override
    public MapState get() {
        return mapState;
    }

    @Override
    public void set(MapState mapState) {
        this.mapState = mapState;
    }
}
