package com.gft.mstime.infraestructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TIME_ADVANCED_ROUTING_KEY = "time.advanced.v1";
    public static final String EXCHANGE = "ms-time.exchange";
    public static final String TIME_ADVANCED_QUEUE = "ms-time.time-advanced.q";
    public static final String DLQ = "ms-time.time-advanced.dlq";
    public static final String DLX = EXCHANGE + ".dlx";

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.gft.mstime");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public TopicExchange timeExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue timeAdvancedQueue() {
        return QueueBuilder.durable(TIME_ADVANCED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .build();
    }

    @Bean
    public Binding timeAdvancedBinding(Queue timeAdvancedQueue, TopicExchange timeExchange) {
        return BindingBuilder.bind(timeAdvancedQueue).to(timeExchange).with(TIME_ADVANCED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("#");
    }
}
