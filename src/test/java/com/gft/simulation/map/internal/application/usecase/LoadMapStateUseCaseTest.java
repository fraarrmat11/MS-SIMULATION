package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.port.out.WarehousePositionPort;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.MapState;
import com.gft.simulation.map.internal.domain.TruckPosition;
import com.gft.simulation.map.internal.domain.WarehousePosition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoadMapStateUseCaseTest {

    TruckPositionPort truckPort = mock(TruckPositionPort.class);
    WarehousePositionPort warehousePort = mock(WarehousePositionPort.class);

    LoadMapStateUseCase useCase = new LoadMapStateUseCase(truckPort, warehousePort);

    @Test
    void shouldLoadFullMapState() {
        when(truckPort.findAll()).thenReturn(List.of(
                new TruckPosition(UUID.randomUUID(), new Location(1, 1))
        ));
        when(warehousePort.findAll()).thenReturn(List.of(
                new WarehousePosition(UUID.randomUUID(), "w1", new Location(2, 2), "FACTORY")
        ));

        MapState result = useCase.execute();

        assertThat(result.getTrucks()).hasSize(1);
        assertThat(result.getWarehouses()).hasSize(1);
    }

    @Test
    void shouldLoadEmptyMapState() {
        when(truckPort.findAll()).thenReturn(List.of());
        when(warehousePort.findAll()).thenReturn(List.of());

        MapState result = useCase.execute();

        assertThat(result.getTrucks()).isEmpty();
        assertThat(result.getWarehouses()).isEmpty();
    }
}
