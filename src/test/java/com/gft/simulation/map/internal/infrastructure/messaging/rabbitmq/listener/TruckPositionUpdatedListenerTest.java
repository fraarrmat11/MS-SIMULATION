package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.UpdateTruckPositionUseCase;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckPositionUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TruckPositionUpdatedListenerTest {

    @Mock
    private UpdateTruckPositionUseCase useCase;

    @InjectMocks
    private TruckPositionUpdatedListener listener;

    @Test
    void shouldDelegateToUseCase() {
        TruckPositionUpdatedEvent event =
                new TruckPositionUpdatedEvent(UUID.randomUUID(), new Location(1, 1));

        listener.onEvent(event);

        verify(useCase).execute(event.getTruckId(), event.getLocation());
    }

    @Test
    void shouldNotPropagateIllegalArgumentException() {
        TruckPositionUpdatedEvent event =
                new TruckPositionUpdatedEvent(UUID.randomUUID(), new Location(1, 1));

        doThrow(new IllegalArgumentException("Truck not found: "))
                .when(useCase)
                .execute(event.getTruckId(), event.getLocation());

        assertDoesNotThrow(
                () -> listener.onEvent(event)
        );

        verify(useCase).execute(event.getTruckId(), event.getLocation());
    }
}
