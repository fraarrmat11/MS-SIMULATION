package com.gft.msmap.infraestructure.web;

import com.gft.msmap.application.service.GetMapStateService;
import com.gft.msmap.domain.MapState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@AllArgsConstructor
@Tag(name = "Map", description = "API for operations on map")
public class MapController {
    private final GetMapStateService getMapStateService;

    @GetMapping
    @Operation(summary = "Obtain complete map")
    @ApiResponse(
            responseCode = "200",
            description = "Map state obtained correctly",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MapState.class)
            )
    )
    public MapState getMapState(){
        return getMapStateService.getMapState();
    }
}
