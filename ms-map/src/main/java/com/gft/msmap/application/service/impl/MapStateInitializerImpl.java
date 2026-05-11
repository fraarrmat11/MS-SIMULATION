package com.gft.msmap.application.service.impl;

import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.application.service.MapStateInitializer;
import com.gft.msmap.application.usecase.LoadMapStateUseCase;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapStateInitializerImpl implements MapStateInitializer {

    private final LoadMapStateUseCase loadMapStateUseCase;
    private final MapStateHolder holder;

    @Override
    @PostConstruct
    public void initializeMapFromBD() {
        holder.set(loadMapStateUseCase.execute());
    }
}
