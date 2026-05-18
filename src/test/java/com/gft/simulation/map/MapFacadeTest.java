package com.gft.simulation.map;

import com.gft.simulation.map.internal.application.service.GetMapStateService;
import com.gft.simulation.map.internal.domain.MapState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class MapFacadeTest {

    private final GetMapStateService getMapStateService = mock(GetMapStateService.class);

    private final MapFacade mapFacade = new MapFacade(getMapStateService);

    @Test
    void getMapState_ShouldDelegateToService() {
        MapState expected = new MapState();
        when(getMapStateService.getMapState()).thenReturn(expected);

        MapState result = mapFacade.getMapState();

        assertThat(result).isSameAs(expected);
        verify(getMapStateService).getMapState();
        verifyNoMoreInteractions(getMapStateService);
    }
}
