// RUTA: ms-map/src/test/java/com/gft/msmap/integration/MapListenersIT.java

package com.gft.msmap.integration;

import com.gft.msmap.application.service.MapStateHolder;
import com.gft.msmap.infraestructure.config.RabbitMQConfig;
import com.gft.msmap.infraestructure.messaging.rabbitmq.TruckPositionUpdatedEvent;
import com.gft.msmap.infraestructure.messaging.rabbitmq.TruckRegisteredEvent;
import com.gft.msmap.infraestructure.messaging.rabbitmq.WarehouseRegisteredEvent;
import com.gft.msmap.infraestructure.persistence.jpa.TruckPositionJpaRepository;
import com.gft.msmap.infraestructure.persistence.jpa.WarehousePositionJpaRepository;
import com.gft.msmap.domain.Location;
import com.gft.msmap.domain.WarehouseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.junit.RabbitAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@Testcontainers
class MapListenersIT {

    @Container
    static RabbitMQContainer rabbit =
            new RabbitMQContainer("rabbitmq:3.13-management-alpine");

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MapStateHolder mapStateHolder;

    @Autowired
    TruckPositionJpaRepository truckRepo;

    @Autowired
    WarehousePositionJpaRepository warehouseRepo;

    @BeforeEach
    void cleanUp() {
        truckRepo.deleteAll();
        warehouseRepo.deleteAll();
        mapStateHolder.set(new com.gft.msmap.domain.MapState());
    }

    @Test
    @DisplayName("TruckRegistered - el camión aparece en MapState y en BD")
    void truckRegistered_updatesMapStateAndPersists() {
        UUID truckId = UUID.randomUUID();
        TruckRegisteredEvent event = new TruckRegisteredEvent(truckId, new Location(10, 20));

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.TRUCK_REGISTERED_ROUTING_KEY, event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(mapStateHolder.get().getTrucks())
                    .anyMatch(t -> t.getTruckId().equals(truckId)
                            && t.getLocation().getX() == 10
                            && t.getLocation().getY() == 20);
            assertThat(truckRepo.findById(truckId))
                    .isPresent()
                    .hasValueSatisfying(e -> {
                        assertThat(e.getXEdge()).isEqualTo(10);
                        assertThat(e.getYEdge()).isEqualTo(20);
                    });
        });
    }

    @Test
    @DisplayName("TruckRegistered duplicado - el camión no se registra dos veces en MapState")
    void truckRegistered_duplicate_isIdempotentInMapState() {
        UUID truckId = UUID.randomUUID();
        TruckRegisteredEvent event = new TruckRegisteredEvent(truckId, new Location(5, 5));

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.TRUCK_REGISTERED_ROUTING_KEY, event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(mapStateHolder.get().getTrucks()).hasSize(1)
        );

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.TRUCK_REGISTERED_ROUTING_KEY, event);

        await().during(Duration.ofSeconds(2)).atMost(Duration.ofSeconds(4)).untilAsserted(() ->
                assertThat(mapStateHolder.get().getTrucks()).hasSize(1)
        );
    }

    @Test
    @DisplayName("TruckPositionUpdated - actualiza posición en MapState y en BD")
    void truckPositionUpdated_updatesMapStateAndPersists() {
        UUID truckId = UUID.randomUUID();

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.TRUCK_REGISTERED_ROUTING_KEY,
                new TruckRegisteredEvent(truckId, new Location(0, 0)));

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(mapStateHolder.get().getTrucks()).anyMatch(t -> t.getTruckId().equals(truckId))
        );

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.TRUCK_POSITION_UPDATED_ROUTING_KEY,
                new TruckPositionUpdatedEvent(truckId, new Location(99, 77)));

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(mapStateHolder.get().getTrucks())
                    .anyMatch(t -> t.getTruckId().equals(truckId)
                            && t.getLocation().getX() == 99
                            && t.getLocation().getY() == 77);
            assertThat(truckRepo.findById(truckId))
                    .isPresent()
                    .hasValueSatisfying(e -> {
                        assertThat(e.getXEdge()).isEqualTo(99);
                        assertThat(e.getYEdge()).isEqualTo(77);
                    });
        });
    }

    @Test
    @DisplayName("WarehouseRegistered - el almacén aparece en MapState y en BD")
    void warehouseRegistered_updatesMapStateAndPersists() {
        UUID warehouseId = UUID.randomUUID();
        WarehouseRegisteredEvent event = new WarehouseRegisteredEvent(
                warehouseId, "Almacén Central", new Location(30, 40), WarehouseType.FACTORY);

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.WAREHOUSE_REGISTERED_ROUTING_KEY, event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(mapStateHolder.get().getWarehouses())
                    .anyMatch(w -> w.getWarehouseId().equals(warehouseId)
                            && w.getName().equals("Almacén Central")
                            && w.getType() == WarehouseType.FACTORY
                            && w.getLocation().getX() == 30
                            && w.getLocation().getY() == 40);
            assertThat(warehouseRepo.findById(warehouseId))
                    .isPresent()
                    .hasValueSatisfying(e -> {
                        assertThat(e.getName()).isEqualTo("Almacén Central");
                        assertThat(e.getWarehouseType()).isEqualTo(WarehouseType.FACTORY);
                        assertThat(e.getXEdge()).isEqualTo(30);
                        assertThat(e.getYEdge()).isEqualTo(40);
                    });
        });
    }

    @Test
    @DisplayName("WarehouseRegistered - tipo CLIENT se persiste correctamente")
    void warehouseRegistered_clientType_persistsCorrectly() {
        UUID warehouseId = UUID.randomUUID();
        WarehouseRegisteredEvent event = new WarehouseRegisteredEvent(
                warehouseId, "Cliente Norte", new Location(1, 2), WarehouseType.CLIENT);

        rabbitTemplate.convertAndSend("trucks.exchange", RabbitMQConfig.WAREHOUSE_REGISTERED_ROUTING_KEY, event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(warehouseRepo.findById(warehouseId))
                        .isPresent()
                        .hasValueSatisfying(e -> assertThat(e.getWarehouseType()).isEqualTo(WarehouseType.CLIENT))
        );
    }
}