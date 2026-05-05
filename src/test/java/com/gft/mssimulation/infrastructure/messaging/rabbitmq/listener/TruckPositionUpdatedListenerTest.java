package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.application.mapstate.UpdateTruckPositionUseCase;
import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import com.gft.mssimulation.application.mapstate.MapStateHolder;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.TruckPositionUpdatedEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TruckPositionUpdatedListenerTest {

    @Test
    void shouldCallUseCaseWhenEventReceived() {

        // GIVEN
        UpdateTruckPositionUseCase useCase = mock(UpdateTruckPositionUseCase.class);

        TruckPositionUpdatedListener listener =
                new TruckPositionUpdatedListener(useCase);

        UUID truckId = UUID.randomUUID();
        Location location = new Location(5, 5);

        TruckPositionUpdatedEvent event =
                new TruckPositionUpdatedEvent(truckId, location);

        // WHEN
        listener.onEvent(event);

        // THEN
        verify(useCase).execute(truckId, location);
    }
}
