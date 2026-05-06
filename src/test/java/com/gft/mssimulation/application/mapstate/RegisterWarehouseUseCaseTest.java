package com.gft.mssimulation.application.mapstate;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.MapState;
import com.gft.mssimulation.domain.mapstate.WarehouseType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.UUID;

public class RegisterWarehouseUseCaseTest {

    @Test
    void shouldRegisterWarehouseToMapState(){

        //GIVEN
        MapState mapState = new MapState();
        UUID warehouseId = UUID.randomUUID();
        int initialSize = mapState.getWarehouses().size();

        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        RegisterWarehouseUseCase useCase = new RegisterWarehouseUseCase(holder);

        //WHEN
        useCase.execute(warehouseId, "warehouseTest", new Location(1,1), WarehouseType.FACTORY);

        //THEN
        assertThat(mapState.getWarehouses())
                .hasSize(initialSize + 1)
                .extracting("warehouseId")
                .contains(warehouseId);

    }

    @Test
    void shouldThrowIfLocationEdgeIsNegative(){
        //GIVEN
        MapState mapState = new MapState();
        UUID warehouseId = UUID.randomUUID();
        MapStateHolder holder = new MapStateHolder();
        holder.set(mapState);

        RegisterWarehouseUseCase useCase = new RegisterWarehouseUseCase(holder);

        //THEN
        assertThatThrownBy(() -> useCase
                .execute(warehouseId, "warehouseTest", new Location(-1,-1), WarehouseType.FACTORY))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
