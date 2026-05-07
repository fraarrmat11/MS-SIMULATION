package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.MapState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetMapStateService {
    private final MapStateHolder holder;

    public MapState getMapState(){
        return holder.get();
    }
}
