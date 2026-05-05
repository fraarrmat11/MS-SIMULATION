package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.MapState;
import org.springframework.stereotype.Service;

@Service
public class MapStateHolder {

    private MapState mapState;

    public MapState get() {
        return mapState;
    }

    public void set(MapState mapState) {
        this.mapState = mapState;
    }
}