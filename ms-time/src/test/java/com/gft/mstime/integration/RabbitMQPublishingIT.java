package com.gft.mstime.integration;

import com.gft.mstime.infraestructure.config.RabbitMQConfig;
import com.gft.mstime.infraestructure.persistence.jpa.SpringDataSimulationClockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.junit.RabbitAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Testcontainers
class RabbitMQPublishingIT {

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
    MockMvc mockMvc;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    SpringDataSimulationClockRepository clockRepository;

    @BeforeEach
    void cleanUp() {
        clockRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /tick/1 - publica un mensaje en la queue con el payload correcto")
    void advanceTime_publishesMessageToQueue() throws Exception {
        mockMvc.perform(post("/tick/1"))
                .andExpect(status().isOk());

        Message message = rabbitTemplate.receive(RabbitMQConfig.TIME_ADVANCED_QUEUE, 5000);

        assertThat(message).isNotNull();
        String body = new String(message.getBody());
        assertThat(body)
                .contains("\"previousDay\":0")
                .contains("\"currentDay\":1")
                .contains("\"daysAdvanced\":1")
                .contains("\"eventId\"")
                .contains("\"occurredAt\"");
    }

    @Test
    @DisplayName("POST /tick/5 - el mensaje publicado refleja los días correctos")
    void advanceTime_fiveDays_messageContainsCorrectDays() throws Exception {
        mockMvc.perform(post("/tick/5"))
                .andExpect(status().isOk());

        Message message = rabbitTemplate.receive(RabbitMQConfig.TIME_ADVANCED_QUEUE, 5000);

        assertThat(message).isNotNull();
        String body = new String(message.getBody());
        assertThat(body)
                .contains("\"previousDay\":0")
                .contains("\"currentDay\":5")
                .contains("\"daysAdvanced\":5");
    }

    @Test
    @DisplayName("Dos ticks consecutivos publican dos mensajes con días acumulados")
    void twoTicks_publishesTwoMessagesWithAccumulatedDays() throws Exception {
        mockMvc.perform(post("/tick/1")).andExpect(status().isOk());
        mockMvc.perform(post("/tick/2")).andExpect(status().isOk());

        Message first  = rabbitTemplate.receive(RabbitMQConfig.TIME_ADVANCED_QUEUE, 5000);
        Message second = rabbitTemplate.receive(RabbitMQConfig.TIME_ADVANCED_QUEUE, 5000);

        assertThat(first).isNotNull();
        assertThat(second).isNotNull();

        String secondBody = new String(second.getBody());
        assertThat(secondBody)
                .contains("\"previousDay\":1")
                .contains("\"currentDay\":3")
                .contains("\"daysAdvanced\":2");
    }
}