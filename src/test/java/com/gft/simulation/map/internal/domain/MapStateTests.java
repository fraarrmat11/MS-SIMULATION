package com.gft.simulation.map.internal.domain;

import com.gft.simulation.map.internal.domain.exceptions.InvalidLocationException;
import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.domain.exceptions.WarehouseAlreadyRegisteredException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MapStateTests {

    MapState mapState = new MapState();

    @Test
    void registerTruck_WhenGivenCorrectArguments_ShouldWork() {
        UUID truckId = UUID.randomUUID();
        int initialSize = mapState.getTrucks().size();
        mapState.registerTruck(truckId, new Location(1, 1));
        assertThat(mapState.getTrucks())
                .hasSize(initialSize + 1)
                .extracting("truckId")
                .contains(truckId);
    }

    @Test
    void registerTruck_WhenGivenExistingTruck_ShouldThrow() {
        UUID truckId = UUID.randomUUID();
        int initialSize = mapState.getTrucks().size();
        mapState.registerTruck(truckId, new Location(1, 1));
        assertThatThrownBy(() -> mapState.registerTruck(truckId, new Location(2, 2))).isInstanceOf(TruckAlreadyRegisteredException.class);
        assertThat(mapState.getTrucks())
                .hasSize(initialSize + 1);
    }

    @Test
    void registerTruck_WhenGivenNegativeEdges_ShouldFail() {
        assertThatThrownBy(() ->
                mapState.registerTruck(UUID.randomUUID(), new Location(-1, -1)))
                .isInstanceOf(InvalidLocationException.class);
    }

    @Test
    void updateTruckPosition_WhenGivenCorrectArguments_ShouldWork() {
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1, 1));
        mapState.updateTruckPosition(truckId, new Location(2, 1));
        assertThat(mapState.getTrucks().get(0).getLocation().getX()).isEqualTo(2);
    }

    @Test
    void updateTruckPosition_WhenGivenNonExistingTruckId_ShouldFail() {
        assertThatThrownBy(() ->
                mapState.updateTruckPosition(UUID.randomUUID(), new Location(1, 1)))
                .isInstanceOf(TruckNotFoundException.class);
    }

    @Test
    void updateTruckPosition_WhenGivenNegativeEdges_ShouldFail() {
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1, 1));
        assertThatThrownBy(() ->
                mapState.updateTruckPosition(UUID.randomUUID(), new Location(-1, -1)))
                .isInstanceOf(InvalidLocationException.class);
    }

    @Test
    void registerWarehouse_WhenGivenCorrectArguments_ShouldWork() {
        UUID warehouseId = UUID.randomUUID();
        int initialSize = mapState.getWarehouses().size();
        mapState.registerWarehouse(warehouseId, "testWarehouse", new Location(1, 1), WarehouseType.FACTORY);
        assertThat(mapState.getWarehouses())
                .hasSize(initialSize + 1)
                .extracting("warehouseId")
                .contains(warehouseId);
    }

    @Test
    void registerWarehouse_WhenGivenExistingWarehouse_ShouldThrow() {
        UUID warehouseId = UUID.randomUUID();
        int initialSize = mapState.getWarehouses().size();
        mapState.registerWarehouse(warehouseId, "Warehouse 1", new Location(1, 1), WarehouseType.FACTORY);

        assertThatThrownBy(() -> mapState.registerWarehouse(warehouseId, "Warehouse 1", new Location(1, 1), WarehouseType.FACTORY))
                .isInstanceOf(WarehouseAlreadyRegisteredException.class);

        assertThat(mapState.getWarehouses())
                .hasSize(initialSize + 1);
    }

    @Test
    void deleteTruck_WhenGivenExistingTruckId_ShouldRemoveTruck() {
        UUID truckId = UUID.randomUUID();
        mapState.registerTruck(truckId, new Location(1, 1));
        int sizeAfterRegister = mapState.getTrucks().size();

        mapState.deleteTruck(truckId);

        assertThat(mapState.getTrucks())
                .hasSize(sizeAfterRegister - 1)
                .extracting("truckId")
                .doesNotContain(truckId);
    }

    @Test
    void deleteTruck_WhenGivenNonExistingTruckId_ShouldThrow() {
        assertThatThrownBy(() -> mapState.deleteTruck(UUID.randomUUID()))
                .isInstanceOf(TruckNotFoundException.class);
    }

    @Test
    void deleteTruck_WhenMultipleTrucksExist_ShouldOnlyDeleteTargetTruck() {
        UUID truckToDelete = UUID.randomUUID();
        UUID otherTruck = UUID.randomUUID();
        mapState.registerTruck(truckToDelete, new Location(1, 1));
        mapState.registerTruck(otherTruck, new Location(2, 2));

        mapState.deleteTruck(truckToDelete);

        assertThat(mapState.getTrucks())
                .extracting("truckId")
                .doesNotContain(truckToDelete)
                .contains(otherTruck);
    }


}
