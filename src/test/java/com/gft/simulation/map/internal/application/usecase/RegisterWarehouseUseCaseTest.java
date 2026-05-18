package com.gft.simulation.map.internal.application.usecase;

import com.gft.simulation.map.internal.application.port.out.WarehousePositionPort;
import com.gft.simulation.map.internal.application.service.MapStateHolder;
import com.gft.simulation.map.internal.application.service.impl.InMemoryMapStateHolder;
import com.gft.simulation.map.internal.domain.Location;
import com.gft.simulation.map.internal.domain.MapState;
import com.gft.simulation.map.internal.domain.WarehouseType;
import com.gft.simulation.map.internal.domain.exceptions.InvalidLocationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class RegisterWarehouseUseCaseTest {

    @Test
    void shouldRegisterWarehouseToMapState() {
        MapState mapState = new MapState();
        UUID warehouseId = UUID.randomUUID();
        int initialSize = mapState.getWarehouses().size();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);
        WarehousePositionPort repository = mock(WarehousePositionPort.class);

        RegisterWarehouseUseCase useCase = new RegisterWarehouseUseCase(holder, repository);

        useCase.execute(warehouseId, "warehouseTest", new Location(1, 1), WarehouseType.FACTORY);

        assertThat(mapState.getWarehouses())
                .hasSize(initialSize + 1)
                .extracting("warehouseId")
                .contains(warehouseId);

        verify(repository).save(any());
    }

    @Test
    void shouldThrowIfLocationEdgeIsNegative() {
        MapState mapState = new MapState();
        UUID warehouseId = UUID.randomUUID();
        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        WarehousePositionPort repository = mock(WarehousePositionPort.class);

        RegisterWarehouseUseCase useCase = new RegisterWarehouseUseCase(holder, repository);

        assertThatThrownBy(() -> useCase
                .execute(warehouseId, "warehouseTest", new Location(-1, -1), WarehouseType.FACTORY))
                .isInstanceOf(InvalidLocationException.class);

        verify(repository, never()).save(any());
    }
}
