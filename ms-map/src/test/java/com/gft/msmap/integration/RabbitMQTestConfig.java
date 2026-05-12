
package com.gft.msmap.integration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class RabbitMQTestConfig {

    @Bean
    public TopicExchange trucksExchangeForTests() {
        return new TopicExchange("trucks.exchange");
    }
}