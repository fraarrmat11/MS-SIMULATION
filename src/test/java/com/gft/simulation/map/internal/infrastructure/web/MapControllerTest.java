package com.gft.simulation.map.internal.infrastructure.web;

import com.gft.simulation.map.internal.application.service.GetMapStateService;
import com.gft.simulation.map.internal.domain.MapState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MapControllerTest {

    @Test
    void shouldReturnsMapStateFromService() {
        GetMapStateService service = mock(GetMapStateService.class);
        MapState expectedMap = new MapState();

        when(service.getMapState()).thenReturn(expectedMap);

        MapController controller = new MapController(service);

        assertThat(controller.getMapState()).isEqualTo(expectedMap);
        verify(service).getMapState();
    }
}
