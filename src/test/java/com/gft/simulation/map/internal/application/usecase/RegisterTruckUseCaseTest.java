package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.TruckPositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.application.service.impl.InMemoryMapStateHolder;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.MapState;
import com.gft.simulation.map.internal.domain.exceptions.InvalidLocationException;
import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class RegisterTruckUseCaseTest {

    @Test
    void shouldRegisterTruckInMapState() {
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        int initialSize = mapState.getTrucks().size();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder, repository);

        useCase.execute(truckId, new Location(1, 1));

        assertThat(holder.get().getTrucks())
                .hasSize(initialSize + 1)
                .extracting("truckId")
                .contains(truckId);

        verify(repository).save(any());
    }

    @Test
    void shouldThrowIfLocationEdgeIsNegative() {
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder, repository);

        assertThatThrownBy(() -> useCase.execute(truckId, new Location(-1, -1))).isInstanceOf(InvalidLocationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void execute_WhenGivenExistingTruck_ShouldThrow() {
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1, 1));

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder, repository);

        assertThatThrownBy(() -> useCase.execute(truckId, new Location(1, 1)))
                .isInstanceOf(TruckAlreadyRegisteredException.class);

        verify(repository, never()).save(any());
    }
}
