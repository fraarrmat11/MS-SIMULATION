package com.gft.simulation.map;

import com.gft.simulation.map.internal.application.service.GetMapStateService;
import com.gft.simulation.map.internal.domain.MapState;
import org.springframework.stereotype.Service;

@Service
public class MapFacade {

    private final GetMapStateService getMapStateService;

    public MapFacade(GetMapStateService getMapStateService) {
        this.getMapStateService = getMapStateService;
    }

    public MapState getMapState() {
        return getMapStateService.getMapState();
    }
}
