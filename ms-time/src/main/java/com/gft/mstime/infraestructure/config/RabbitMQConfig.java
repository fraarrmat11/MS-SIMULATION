package com.gft.mstime.infraestructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String TRUCK_REGISTERED_QUEUE = "truck.registered.v1";
    public static final String TRUCK_POSITION_UPDATED_QUEUE = "truck.position.updated.v1";
    public static final String WAREHOUSE_REGISTERED_QUEUE = "warehouse.registered.v1";

    @Bean
    Queue truckRegisteredQueue() {
        return new Queue(TRUCK_REGISTERED_QUEUE, true);
    }

    @Bean
    Queue truckPositionUpdatedQueue() {
        return new Queue(TRUCK_POSITION_UPDATED_QUEUE, true);
    }

    @Bean
    Queue warehouseRegisteredQueue() {
        return new Queue(WAREHOUSE_REGISTERED_QUEUE, true);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        typeMapper.addTrustedPackages("com.gft.mstime");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
