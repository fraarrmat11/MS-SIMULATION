package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import org.junit.jupiter.api.Test;
import com.gft.mssimulation.domain.mapstate.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        // WHEN
        useCase.execute(truckId, new Location(10, 20));

        // THEN
        TruckPosition updated = holder.get().getTrucks().stream()
                .filter(t -> t.getTruckId().equals(truckId))
                .findFirst()
                .orElseThrow();

        assertThat(updated.getLocation().getX()).isEqualTo(10);
        assertThat(updated.getLocation().getY()).isEqualTo(20);
    }

    @Test
    void shouldThrowIfTruckDoesNotExist() {

        MapState mapState = new MapState();

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        UpdateTruckPositionUseCase useCase =
                new UpdateTruckPositionUseCase(holder);

        UUID unknownTruckId = UUID.randomUUID();

        assertThatThrownBy(() ->
                useCase.execute(unknownTruckId, new Location(1, 1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}