package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.RegisterWarehouseUseCase;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.WarehouseRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WarehouseRegisteredListenerTest {

    @Mock
    private RegisterWarehouseUseCase useCase;

    @InjectMocks
    private WarehouseRegisteredListener listener;

    @Test
    void shouldDelegateEventToUseCase() {
        WarehouseRegisteredEvent event = new WarehouseRegisteredEvent(UUID.randomUUID(), "test", new Location(1, 1), "FACTORY");

        listener.onEvent(event);

        verify(useCase).execute(event.getWarehouseId(), event.getName(), event.getLocation(), event.getWarehouseType());
    }
}
