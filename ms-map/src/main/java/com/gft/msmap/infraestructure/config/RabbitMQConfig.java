package com.gft.msmap.infraestructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String EXCHANGE = "ms-map.events";

    public static final String TRUCK_REGISTERED_ROUTING_KEY = "truck.registered.v1";
    public static final String TRUCK_POSITION_UPDATED_ROUTING_KEY = "truck.position.updated.v1";
    public static final String WAREHOUSE_REGISTERED_ROUTING_KEY = "warehouse.registered.v1";

    public static final String TRUCK_REGISTERED_QUEUE = "ms-map.truck-registered.q";
    public static final String TRUCK_POSITION_UPDATED_QUEUE = "ms-map.truck-position-updated.q";
    public static final String WAREHOUSE_REGISTERED_QUEUE = "ms-map.warehouse-registered.q";


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
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
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.gft.mstime");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public Binding truckRegisteredBinding() {
        return BindingBuilder.bind(truckRegisteredQueue())
                .to(exchange())
                .with(TRUCK_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Binding truckPositionUpdatedBinding() {
        return BindingBuilder.bind(truckPositionUpdatedQueue())
                .to(exchange())
                .with(TRUCK_POSITION_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding warehouseRegisteredBinding() {
        return BindingBuilder.bind(warehouseRegisteredQueue())
                .to(exchange())
                .with(WAREHOUSE_REGISTERED_ROUTING_KEY);
    }

}
