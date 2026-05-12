// RUTA: ms-map/src/test/java/com/gft/msmap/integration/MapControllerIT.java

package com.gft.msmap.integration;

import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.MapState;
import com.gft.msmap.domain.WarehouseType;
import com.gft.msmap.infraestructure.persistence.jpa.TruckPositionJpaRepository;
import com.gft.msmap.infraestructure.persistence.jpa.WarehousePositionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.junit.RabbitAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de integración para MapController.
 *
 * - BD: H2 en memoria (perfil "integration-test")
 * - RabbitMQ: broker embebido de spring-rabbit-test (@RabbitAvailable)
 *   Sin Docker, sin Testcontainers, sin conexión externa.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@RabbitAvailable
class MapControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MapStateHolder mapStateHolder;

    @Autowired
    TruckPositionJpaRepository truckRepo;

    @Autowired
    WarehousePositionJpaRepository warehouseRepo;

    private static final UUID TRUCK_ID     = UUID.randomUUID();
    private static final UUID WAREHOUSE_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        truckRepo.deleteAll();
        warehouseRepo.deleteAll();

        MapState state = new MapState();
        state.registerTruck(TRUCK_ID, new Location(10, 20));
        state.registerWarehouse(WAREHOUSE_ID, "Almacén Test", new Location(50, 60), WarehouseType.PRODUCTION);
        mapStateHolder.set(state);
    }

    @Test
    @DisplayName("GET /map - devuelve 200 con la lista de trucks y warehouses")
    void getMap_returnsTrucksAndWarehouses() throws Exception {
        mockMvc.perform(get("/map").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trucks").isArray())
                .andExpect(jsonPath("$.trucks[0].truckId").value(TRUCK_ID.toString()))
                .andExpect(jsonPath("$.trucks[0].location.x").value(10))
                .andExpect(jsonPath("$.trucks[0].location.y").value(20))
                .andExpect(jsonPath("$.warehouses").isArray())
                .andExpect(jsonPath("$.warehouses[0].warehouseId").value(WAREHOUSE_ID.toString()))
                .andExpect(jsonPath("$.warehouses[0].name").value("Almacén Test"))
                .andExpect(jsonPath("$.warehouses[0].type").value("PRODUCTION"))
                .andExpect(jsonPath("$.warehouses[0].location.x").value(50))
                .andExpect(jsonPath("$.warehouses[0].location.y").value(60));
    }

    @Test
    @DisplayName("GET /map - mapa vacío devuelve listas vacías")
    void getMap_emptyState_returnsEmptyLists() throws Exception {
        mapStateHolder.set(new MapState());

        mockMvc.perform(get("/map").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trucks").isArray())
                .andExpect(jsonPath("$.trucks").isEmpty())
                .andExpect(jsonPath("$.warehouses").isArray())
                .andExpect(jsonPath("$.warehouses").isEmpty());
    }

    @Test
    @DisplayName("GET /map - múltiples trucks en el estado se devuelven todos")
    void getMap_multipleTrucks_returnsAll() throws Exception {
        MapState state = new MapState();
        UUID truck1 = UUID.randomUUID();
        UUID truck2 = UUID.randomUUID();
        state.registerTruck(truck1, new Location(1, 1));
        state.registerTruck(truck2, new Location(2, 2));
        mapStateHolder.set(state);

        mockMvc.perform(get("/map").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trucks.length()").value(2));
    }
}