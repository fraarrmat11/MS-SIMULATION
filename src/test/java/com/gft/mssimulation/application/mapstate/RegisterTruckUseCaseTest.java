package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

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

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder);

        //WHEN
        useCase.execute(truckId, new Location(1,1));

        //THEN
        assertThat(holder.get().getTrucks())
                .hasSize(initialSize + 1)
                .extracting("truckId")
                .contains(truckId);
    }

    @Test
    void shouldThrowIfLocationEdgeIsNegative(){
        //GIVEN
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        RegisterTruckUseCase useCase = new RegisterTruckUseCase(holder);
        //THEN
        assertThatThrownBy(() -> useCase.execute(truckId, new Location(-1,-1))).isInstanceOf(IllegalArgumentException.class);
    }
}
