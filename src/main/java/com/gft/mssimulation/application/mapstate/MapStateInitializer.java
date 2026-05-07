package com.gft.mssimulation.application.mapstate;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapStateInitializer {
    private final LoadMapStateUseCase loadMapStateUseCase;
    private final MapStateHolder holder;

    @PostConstruct
    public void initializeMapFromBD(){
        holder.set(loadMapStateUseCase.execute());
    }
}
