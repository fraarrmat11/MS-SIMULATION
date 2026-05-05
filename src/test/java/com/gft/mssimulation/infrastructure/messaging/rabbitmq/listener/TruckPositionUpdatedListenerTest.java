package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import com.gft.mssimulation.application.mapstate.MapStateHolder;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TruckPositionUpdatedEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckPositionUpdatedListenerTest {
    @Test
    void shouldUpdateTruckPositionWhenEventReceived() {

        // GIVEN
        MapState mapState = new MapState();
        UUID truckId = UUID.randomUUID();

        mapState.registerTruck(truckId, new Location(1, 1));

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        TruckPositionUpdatedListener listener =
                new TruckPositionUpdatedListener(holder);

        TruckPositionUpdatedEvent event =
                new TruckPositionUpdatedEvent(truckId, new Location(5, 5));

        // WHEN
        listener.onEvent(event);

        // THEN
        assertThat(holder.get().getTrucks())
                .filteredOn(t -> t.getTruckId().equals(truckId))
                .singleElement()
                .extracting(t -> t.getLocation().getX())
                .isEqualTo(5);
    }
}
