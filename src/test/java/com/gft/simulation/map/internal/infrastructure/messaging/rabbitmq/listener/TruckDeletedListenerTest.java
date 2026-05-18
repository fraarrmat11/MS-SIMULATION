package com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.listener;

import com.gft.simulation.map.internal.application.usecase.DeleteTruckUseCase;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.infrastructure.messaging.rabbitmq.TruckDeletedEvent;
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
class TruckDeletedListenerTest {

    @Mock
    private DeleteTruckUseCase useCase;

    @InjectMocks
    private TruckDeletedListener listener;

    @Test
    void shouldDelegateToUseCase() {
        TruckDeletedEvent event = new TruckDeletedEvent(UUID.randomUUID());

        listener.onEvent(event);

        verify(useCase).execute(event.getTruckId());
    }

    @Test
    void shouldNotPropagateTruckNotFoundException() {
        TruckDeletedEvent event = new TruckDeletedEvent(UUID.randomUUID());

        doThrow(new TruckNotFoundException(event.getTruckId()))
                .when(useCase)
                .execute(event.getTruckId());

        assertDoesNotThrow(() -> listener.onEvent(event));

        verify(useCase).execute(event.getTruckId());
    }
}
