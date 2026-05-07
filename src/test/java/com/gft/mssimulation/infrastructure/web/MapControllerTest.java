package com.gft.mssimulation.infrastructure.web;

import com.gft.mssimulation.application.mapstate.GetMapStateService;
import com.gft.mssimulation.domain.mapstate.MapState;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MapControllerTest {

    @Test
    void shouldReturnsMapStateFromService(){

        GetMapStateService service = mock(GetMapStateService.class);

        MapState expectedMap = new MapState();

        when(service.getMapState()).thenReturn(expectedMap);

        MapController controller = new MapController(service);

        assertThat( controller.getMapState()).isEqualTo(expectedMap);
        verify(service).getMapState();
    }



}
