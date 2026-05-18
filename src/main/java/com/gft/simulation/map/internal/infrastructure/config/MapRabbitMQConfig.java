package com.gft.simulation.map.internal.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapRabbitMQConfig {

    public static final String TRUCK_REGISTERED_ROUTING_KEY = "truck.registered.v1";
    public static final String TRUCK_POSITION_UPDATED_ROUTING_KEY = "truck.position.updated.v1";
    public static final String WAREHOUSE_REGISTERED_ROUTING_KEY = "warehouse.registered.v1";

    public static final String TRUCK_REGISTERED_QUEUE = "ms-map.truck-registered.q";
    public static final String TRUCK_POSITION_UPDATED_QUEUE = "ms-map.truck-position-updated.q";
    public static final String WAREHOUSE_REGISTERED_QUEUE = "ms-map.warehouse-registered.q";

    @Bean
    public TopicExchange trucksExchange() {
        return new TopicExchange("trucks.exchange");
    }

    @Bean
    public Queue truckRegisteredQueue() {
        return QueueBuilder.durable(TRUCK_REGISTERED_QUEUE).build();
    }

    @Bean
    public Queue truckPositionUpdatedQueue() {
        return QueueBuilder.durable(TRUCK_POSITION_UPDATED_QUEUE).build();
    }

    @Bean
    public Queue warehouseRegisteredQueue() {
        return QueueBuilder.durable(WAREHOUSE_REGISTERED_QUEUE).build();
    }

    @Bean
    public Binding truckRegisteredBinding() {
        return BindingBuilder.bind(truckRegisteredQueue())
                .to(trucksExchange())
                .with(TRUCK_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Binding truckPositionUpdatedBinding() {
        return BindingBuilder.bind(truckPositionUpdatedQueue())
                .to(trucksExchange())
                .with(TRUCK_POSITION_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding warehouseRegisteredBinding() {
        return BindingBuilder.bind(warehouseRegisteredQueue())
                .to(trucksExchange())
                .with(WAREHOUSE_REGISTERED_ROUTING_KEY);
    }
}
