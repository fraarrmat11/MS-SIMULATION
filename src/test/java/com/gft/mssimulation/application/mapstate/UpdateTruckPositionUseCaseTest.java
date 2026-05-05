package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateTruckPositionUseCaseTest {

    @Test
    void shouldUpdateTruckPositionInMapState() {

        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();

        mapState.registerTruck(truckId, new Location(1, 1));

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        UpdateTruckPositionUseCase useCase =
                new UpdateTruckPositionUseCase(holder);

        useCase.execute(truckId, new Location(10, 10));

        assertThat(holder.get().getTrucks())
                .filteredOn(t -> t.getTruckId().equals(truckId))
                .singleElement()
                .extracting(t -> t.getLocation().getX())
                .isEqualTo(10);
    }
}