package com.gft.msmap.application.service.impl;

import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.domain.MapState;
import org.springframework.stereotype.Service;

@Service
public class InMemoryMapStateHolder implements MapStateHolder {

    private MapState mapState;

    @Override
    public MapState get() {
        return mapState;
    }

    @Override
    public void set(MapState mapState) {
        this.mapState = mapState;
    }
}
