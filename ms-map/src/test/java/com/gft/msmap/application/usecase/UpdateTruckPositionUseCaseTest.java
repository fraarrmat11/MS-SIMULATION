package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.application.service.impl.InMemoryMapStateHolder;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.MapState;
import com.gft.msmap.domain.TruckPosition;
import com.gft.msmap.domain.exceptions.TruckNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateTruckPositionUseCaseTest {

    @Test
    void shouldUpdateTruckPositionInMapState() {

        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();

        mapState.registerTruck(truckId, new Location(1, 1));

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        UpdateTruckPositionUseCase useCase =
                new UpdateTruckPositionUseCase(holder,repository);

        // WHEN
        useCase.execute(truckId, new Location(10, 20));

        // THEN
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

        UpdateTruckPositionUseCase useCase =
                new UpdateTruckPositionUseCase(holder, repository);

        UUID unknownTruckId = UUID.randomUUID();

        assertThatThrownBy(() ->
                useCase.execute(unknownTruckId, new Location(1, 1))
        ).isInstanceOf(TruckNotFoundException.class);

        verify(repository,never()).save(any());
    }
}
