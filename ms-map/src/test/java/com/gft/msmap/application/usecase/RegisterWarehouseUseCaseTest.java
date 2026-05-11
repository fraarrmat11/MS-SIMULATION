package com.gft.msmap.application.usecase;

import com.gft.msmap.application.port.out.WarehousePositionPort;
import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.application.service.impl.InMemoryMapStateHolder;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.MapState;
import com.gft.msmap.domain.WarehouseType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.UUID;
import static org.mockito.Mockito.*;
public class RegisterWarehouseUseCaseTest {

    @Test
    void shouldRegisterWarehouseToMapState(){

        //GIVEN
        MapState mapState = new MapState();
        UUID warehouseId = UUID.randomUUID();
        int initialSize = mapState.getWarehouses().size();

        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);
        WarehousePositionPort repository = mock(WarehousePositionPort.class);

        RegisterWarehouseUseCase useCase = new RegisterWarehouseUseCase(holder, repository);

        //WHEN
        useCase.execute(warehouseId, "warehouseTest", new Location(1,1), WarehouseType.FACTORY);

        //THEN
        assertThat(mapState.getWarehouses())
                .hasSize(initialSize + 1)
                .extracting("warehouseId")
                .contains(warehouseId);

        verify(repository).save(any());
    }

    @Test
    void shouldThrowIfLocationEdgeIsNegative(){
        //GIVEN
        MapState mapState = new MapState();
        UUID warehouseId = UUID.randomUUID();
        MapStateHolder holder = new InMemoryMapStateHolder();
        holder.set(mapState);

        WarehousePositionPort repository = mock(WarehousePositionPort.class);

        RegisterWarehouseUseCase useCase = new RegisterWarehouseUseCase(holder, repository);

        //THEN
        assertThatThrownBy(() -> useCase
                .execute(warehouseId, "warehouseTest", new Location(-1,-1), WarehouseType.FACTORY))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repository, never()).save(any());
    }
}
