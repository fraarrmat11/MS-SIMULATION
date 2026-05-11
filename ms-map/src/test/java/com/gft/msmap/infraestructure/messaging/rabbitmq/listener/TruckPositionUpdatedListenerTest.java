package com.gft.msmap.infraestructure.messaging.rabbitmq.listener;

import com.gft.msmap.application.usecase.UpdateTruckPositionUseCase;
import com.gft.msmap.domain.Location;
import com.gft.msmap.infraestructure.messaging.rabbitmq.TruckPositionUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

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
                new TruckPositionUpdatedEvent(UUID.randomUUID(), new Location(1,1));

        listener.onEvent(event);

        verify(useCase).execute(event.getTruckId(),event.getLocation());
    }
}
