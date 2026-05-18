package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.application.service.impl.InMemoryMapStateHolder;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.MapState;
import com.gft.simulation.map.internal.domain.TruckPosition;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateTruckPositionUseCaseTest {

    @Test
    void shouldUpdateTruckPositionInMapState() {
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1, 1));

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        UpdateTruckPositionUseCase useCase = new UpdateTruckPositionUseCase(holder, repository);

        useCase.execute(truckId, new Location(10, 20));

        TruckPosition updated = holder.get().getTrucks().stream()
                .filter(t -> t.getTruckId().equals(truckId))
                .findFirst()
                .orElseThrow();

        assertThat(updated.getLocation().getX()).isEqualTo(10);
        assertThat(updated.getLocation().getY()).isEqualTo(20);

        verify(repository).save(any());
    }

    @Test
    void shouldThrowIfTruckDoesNotExist() {
        MapState mapState = new MapState();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);
        TruckPositionPort repository = mock(TruckPositionPort.class);

        UpdateTruckPositionUseCase useCase = new UpdateTruckPositionUseCase(holder, repository);

        UUID unknownTruckId = UUID.randomUUID();

        assertThatThrownBy(() ->
                useCase.execute(unknownTruckId, new Location(1, 1))
        ).isInstanceOf(TruckNotFoundException.class);

        verify(repository, never()).save(any());
    }
}
