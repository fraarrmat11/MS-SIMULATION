package com.gft.msmap.application.service.impl;

import com.gft.msmap.application.service.GetMapStateService;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.domain.MapState;
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
