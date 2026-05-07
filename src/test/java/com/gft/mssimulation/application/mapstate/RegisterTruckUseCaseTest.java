package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import com.gft.mssimulation.domain.mapstate.TruckPosition;
import com.gft.mssimulation.infrastructure.persistence.jpa.mapstate.TruckPositionJpaAdapter;
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

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        TruckPositionJpaAdapter repository = mock(TruckPositionJpaAdapter.class);

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

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        TruckPositionJpaAdapter repository = mock(TruckPositionJpaAdapter.class);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder, repository);
        //THEN
        assertThatThrownBy(() -> useCase.execute(truckId, new Location(-1,-1))).isInstanceOf(IllegalArgumentException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldIgnoreAlreadyRegisteredTruck(){
        //GIVEN
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1,1));

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        TruckPositionJpaAdapter repository = mock(TruckPositionJpaAdapter.class);

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
