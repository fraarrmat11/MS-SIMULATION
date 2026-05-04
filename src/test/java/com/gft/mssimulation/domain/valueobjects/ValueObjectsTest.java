package com.gft.mssimulation.domain.valueobjects;

import com.gft.mssimulation.domain.mapstate.Location;
import com.gft.mssimulation.domain.mapstate.TruckPosition;
import com.gft.mssimulation.domain.mapstate.WarehousePosition;
import com.gft.mssimulation.domain.mapstate.WarehouseType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ValueObjectsTest {

    @Test
    void createsLocation() {
        Location location = new Location(10, 20);

        assertThat(location.getX()).isEqualTo(10);
        assertThat(location.getY()).isEqualTo(20);
    }

    @Test
    void createsLocationWithNegativeXEdge(){
        assertThatThrownBy(() ->
                new Location(-1, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createsLocationWithNegativeYEdge(){
        assertThatThrownBy(() ->
                new Location(1, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createsTruckPosition() {
        UUID truckId = UUID.randomUUID();
        Location location = new Location(10, 20);

        TruckPosition truckPosition = new TruckPosition(truckId, location);

        assertThat(truckPosition.getTruckId()).isEqualTo(truckId);
        assertThat(truckPosition.getLocation()).isSameAs(location);
    }

    @Test
    void createsWarehousePosition() {
        UUID warehouseId = UUID.randomUUID();
        Location location = new Location(30, 40);

        WarehousePosition warehousePosition = new WarehousePosition(
                warehouseId,
                "Factory 1",
                location,
                WarehouseType.FACTORY
        );

    }

    @Test
    void exposesWarehouseTypes() {
        assertThat(WarehouseType.values())
                .containsExactly(WarehouseType.FACTORY, WarehouseType.PRODUCTION, WarehouseType.CLIENT);
        assertThat(WarehouseType.valueOf("FACTORY")).isEqualTo(WarehouseType.FACTORY);
    }
}
