package com.gft.mssimulation.application.mapstate;
import com.gft.mssimulation.domain.mapstate.MapState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GetMapStateServiceTest {

    @Test
    void shouldReturnMapState(){

        MapStateHolder holder = mock(MapStateHolder.class);

        MapState expectedMap = new MapState();

        GetMapStateService service = new GetMapStateService(holder);

        when(holder.get()).thenReturn(expectedMap);

        assertThat(service.getMapState()).isEqualTo(expectedMap);
    }
}
