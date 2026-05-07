package com.gft.mssimulation.domain.mapstate;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

public class MapStateTests {

    MapState mapState = new MapState();

    @Test
    void registerTruck_WhenGivenCorrectArguments_ShouldWork(){
        UUID truckId = UUID.randomUUID();
        int initialSize = mapState.getTrucks().size();
        mapState.registerTruck(truckId,new Location(1,1));
        assertThat(mapState.getTrucks())
                .hasSize(initialSize + 1)
                .extracting("truckId")
                .contains(truckId);
    }

    @Test
    void registerTruck_WhenGivenExistingTruck_ShouldThrow(){
        UUID truckId = UUID.randomUUID();
        int initialSize = mapState.getTrucks().size();
        mapState.registerTruck(truckId,new Location(1,1));
        assertThatThrownBy(() -> mapState.registerTruck(truckId,new Location(2,2))).isInstanceOf(IllegalArgumentException.class);
        assertThat(mapState.getTrucks())
                .hasSize(initialSize+1);
    }

    @Test
    void registerTruck_WhenGivenNegativeEdges_ShouldFail(){
        assertThatThrownBy(() ->
                mapState.registerTruck(UUID.randomUUID(),new Location(-1,-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTruckPosition_WhenGivenCorrectArguments_ShouldWork(){
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId,new Location(1,1));
        mapState.updateTruckPosition(truckId, new Location(2,1));
        assertThat(mapState.getTrucks().getFirst().getLocation().getX()).isEqualTo(2);
    }

    @Test
    void updateTruckPosition_WhenGivenNonExistingTruckId_ShouldFail(){
        assertThatThrownBy(() ->
                mapState.updateTruckPosition(UUID.randomUUID(), new Location(1,1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTruckPosition_WhenGivenNegativeEdges_ShouldFail(){
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId,new Location(1,1));
        assertThatThrownBy(() ->
                mapState.updateTruckPosition(UUID.randomUUID(), new Location(-1,-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registerWarehouse_WhenGivenCorrectArguments_ShouldWork(){
        UUID warehouseId = UUID.randomUUID();
        int initialSize = mapState.getWarehouses().size();
        mapState.registerWarehouse(warehouseId,"testWarehosue",new Location(1,1),WarehouseType.FACTORY);
        assertThat(mapState.getWarehouses())
                .hasSize(initialSize + 1)
                .extracting("warehouseId")
                .contains(warehouseId);
    }

    @Test
    void registerWarehouse_WhenGivenNegativeEdges_ShouldFail(){
        assertThatThrownBy(() -> mapState.registerWarehouse(
                UUID.randomUUID(), "testWarehouse", new Location(-1,-1), WarehouseType.FACTORY))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
