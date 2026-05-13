package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.TruckPositionPort;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.application.service.impl.InMemoryMapStateHolder;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.MapState;
import com.gft.msmap.domain.exceptions.InvalidLocationException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import java.util.UUID;

public class RegisterTruckUseCaseTest {

    @Test
    void shouldRegisterTruckInMapState(){

        //GIVEN
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        int initialSize = mapState.getTrucks().size();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder,repository);

        //WHEN
        useCase.execute(truckId, new Location(1,1));

        //THEN
        assertThat(holder.get().getTrucks())
                .hasSize(initialSize + 1)
                .extracting("truckId")
                .contains(truckId);

        verify(repository).save(any());
    }

    @Test
    void shouldThrowIfLocationEdgeIsNegative(){
        //GIVEN
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder, repository);
        //THEN
        assertThatThrownBy(() -> useCase.execute(truckId, new Location(-1,-1))).isInstanceOf(InvalidLocationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldIgnoreAlreadyRegisteredTruck(){
        //GIVEN
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1,1));

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        TruckPositionPort repository = mock(TruckPositionPort.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder, repository);

        //WHEN
        useCase.execute(truckId, new Location(1,1));

        //THEN
        assertThat(holder.get().getTrucks())
                .hasSize(1)
                .extracting("truckId")
                .containsExactly(truckId);

        verify(repository, never()).save(any());
    }
}
