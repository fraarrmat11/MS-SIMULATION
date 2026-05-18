package com.gft.simulation.map.internal.application.service.impl;

import com.gft.simulation.map.internal.application.service.GetMapStateService;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.domain.MapState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetMapStateServiceImpl implements GetMapStateService {

    private final MapStateHolder holder;

    @Override
    public MapState getMapState() {
        return holder.get();
    }
}
