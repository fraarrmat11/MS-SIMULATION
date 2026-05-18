package com.gft.simulation.map.internal.infrastructure.web;

import com.gft.simulation.map.internal.application.service.GetMapStateService;
import com.gft.simulation.map.internal.domain.exceptions.InvalidLocationException;
import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.domain.exceptions.WarehouseAlreadyRegisteredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private final GetMapStateService service = mock(GetMapStateService.class);
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MapController(service))
                .setControllerAdvice(new MapGlobalExceptionHandler())
                .build();
    }

    @Test
    void handleTruckNotFound_ShouldReturn404() throws Exception {
        when(service.getMapState()).thenThrow(new TruckNotFoundException(UUID.randomUUID()));

        mockMvc.perform(get("/map")).andExpect(status().isNotFound());
    }

    @Test
    void handleTruckAlreadyRegistered_ShouldReturn409() throws Exception {
        when(service.getMapState()).thenThrow(new TruckAlreadyRegisteredException(UUID.randomUUID()));

        mockMvc.perform(get("/map")).andExpect(status().isConflict());
    }

    @Test
    void handleWarehouseAlreadyRegistered_ShouldReturn409() throws Exception {
        when(service.getMapState()).thenThrow(new WarehouseAlreadyRegisteredException(UUID.randomUUID()));

        mockMvc.perform(get("/map")).andExpect(status().isConflict());
    }

    @Test
    void handleInvalidLocation_ShouldReturn422() throws Exception {
        when(service.getMapState()).thenThrow(new InvalidLocationException(-1, -1));

        mockMvc.perform(get("/map")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void handleUnexpected_ShouldReturn500() throws Exception {
        when(service.getMapState()).thenThrow(new RuntimeException("unexpected"));

        mockMvc.perform(get("/map")).andExpect(status().isInternalServerError());
    }
}
