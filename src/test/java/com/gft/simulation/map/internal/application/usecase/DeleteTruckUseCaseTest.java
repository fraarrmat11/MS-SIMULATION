package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.application.service.impl.InMemoryMapStateHolder;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.MapState;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteTruckUseCaseTest {

    @Test
    void shouldDeleteTruckFromMapStateAndPersistence() {
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1, 1));

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort port = mock(TruckPositionPort.class);

        DeleteTruckUseCase useCase = new DeleteTruckUseCase(holder, port);

        useCase.execute(truckId);

        assertThat(holder.get().getTrucks())
                .extracting("truckId")
                .doesNotContain(truckId);

        verify(port).delete(truckId);
    }

    @Test
    void shouldThrowIfTruckDoesNotExist() {
        MapState mapState = new MapState();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort port = mock(TruckPositionPort.class);

        DeleteTruckUseCase useCase = new DeleteTruckUseCase(holder, port);

        assertThatThrownBy(() -> useCase.execute(UUID.randomUUID()))
                .isInstanceOf(TruckNotFoundException.class);

        verify(port, never()).delete(any());
    }
}
