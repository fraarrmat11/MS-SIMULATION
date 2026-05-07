package com.gft.mssimulation.infrastructure.web;

import com.gft.mssimulation.application.mapstate.GetMapStateService;
import com.gft.mssimulation.domain.mapstate.MapState;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@AllArgsConstructor
public class MapController {
    private final GetMapStateService getMapStateService;

    @GetMapping
    public MapState getMapState(){
        return getMapStateService.getMapState();
    }
}
