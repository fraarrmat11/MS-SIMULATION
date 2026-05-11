package com.gft.msmap.infraestructure.web;

import com.gft.msmap.application.service.GetMapStateService;
import com.gft.msmap.domain.MapState;
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
