package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.RegisterTruckUseCase;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

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
}
