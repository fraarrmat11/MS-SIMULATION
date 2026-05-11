package com.gft.msmap.application.service.impl;

import com.gft.msmap.application.service.GetMapStateService;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.domain.MapState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GetMapStateServiceImplTest {

    @Test
    void shouldReturnMapState(){

        MapStateHolder holder = mock(MapStateHolder.class);

        MapState expectedMap = new MapState();

        GetMapStateService service = new GetMapStateServiceImpl(holder);

        when(holder.get()).thenReturn(expectedMap);

        assertThat(service.getMapState()).isEqualTo(expectedMap);
    }
}
