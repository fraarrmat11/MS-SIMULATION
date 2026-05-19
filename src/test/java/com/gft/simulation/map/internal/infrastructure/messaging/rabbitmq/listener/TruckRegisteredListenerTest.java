package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.RegisterTruckUseCase;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckRegisteredEvent;
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
class TruckRegisteredListenerTest {

    @Mock
    private RegisterTruckUseCase useCase;

    @InjectMocks
    private TruckRegisteredListener listener;

    @Test
    void shouldDelegateToUseCase() {
        TruckRegisteredEvent event = new TruckRegisteredEvent(UUID.randomUUID(), new Location(1, 1));

        listener.onEvent(event);

        verify(useCase).execute(event.getTruckId(), event.getLocation());
    }

    @Test
    void shouldNotPropagateTruckAlreadyRegisteredException() {
        TruckRegisteredEvent event = new TruckRegisteredEvent(UUID.randomUUID(), new Location(1, 1));

        doThrow(new TruckAlreadyRegisteredException(event.getTruckId()))
                .when(useCase)
                .execute(event.getTruckId(), event.getLocation());

        assertDoesNotThrow(() -> listener.onEvent(event));

        verify(useCase).execute(event.getTruckId(), event.getLocation());
    }
}
