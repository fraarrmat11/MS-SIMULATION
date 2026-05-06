package com.gft.mssimulation.infrastructure.messaging.rabbitmq.listener;

import com.gft.mssimulation.application.mapstate.RegisterWarehouseUseCase;
import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.WarehouseType;
import com.gft.mssimulation.infrastructure.messaging.rabbitmq.message.WarehouseRegisteredEvent;
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
        WarehouseRegisteredEvent event = new WarehouseRegisteredEvent(UUID.randomUUID(), "test", new Location(1,1), WarehouseType.FACTORY);

        listener.onEvent(event);

        verify(useCase).execute(event.getWarehouseId(),event.getName(),event.getLocation(),event.getWarehouseType());
    }
}
