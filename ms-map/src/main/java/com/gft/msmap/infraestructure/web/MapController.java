package com.gft.msmap.infraestructure.web;

import com.gft.msmap.application.service.GetMapStateService;
import com.gft.msmap.domain.MapState;
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
