package com.gft.simulation.time.integration;

import com.gft.simulation.time.internal.application.port.out.TimeAdvancedEventPublisher;
import com.gft.simulation.time.internal.infrastructure.persistence.jpa.SimulationClockEntity;
import com.gft.simulation.time.internal.infrastructure.persistence.jpa.SpringDataSimulationClockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
class SimulationClockControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SpringDataSimulationClockRepository clockRepository;

    @MockitoBean
    TimeAdvancedEventPublisher timeAdvancedEventPublisher;

    @BeforeEach
    void cleanDatabase() {
        clockRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /tick/current - sin registro en BD arranca en día 0")
    void getCurrentDay_whenNoClock_returnsDay0() throws Exception {
        mockMvc.perform(get("/tick/current").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentDay").value(0));
    }

    @Test
    @DisplayName("GET /tick/current - con registro en BD devuelve el día persistido")
    void getCurrentDay_whenClockExists_returnsPersistedDay() throws Exception {
        clockRepository.save(new SimulationClockEntity(1L, 5));

        mockMvc.perform(get("/tick/current").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentDay").value(5));
    }

    @Test
    @DisplayName("POST /tick/1 - avanza 1 día desde 0, persiste y devuelve resultado correcto")
    void advanceTime_oneDayFromZero_persistsAndReturnsResult() throws Exception {
        mockMvc.perform(post("/tick/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previousDay").value(0))
                .andExpect(jsonPath("$.currentDay").value(1))
                .andExpect(jsonPath("$.daysAdvanced").value(1))
                .andExpect(jsonPath("$.eventId").isNotEmpty())
                .andExpect(jsonPath("$.occurredAt").isNotEmpty());

        assertThat(clockRepository.findById(1L))
                .isPresent()
                .hasValueSatisfying(e -> assertThat(e.getCurrentDay()).isEqualTo(1));
    }

    @Test
    @DisplayName("POST /tick/3 - avanza 3 días desde 0, persiste día 3")
    void advanceTime_threeDays_persistsDay3() throws Exception {
        mockMvc.perform(post("/tick/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previousDay").value(0))
                .andExpect(jsonPath("$.currentDay").value(3))
                .andExpect(jsonPath("$.daysAdvanced").value(3));

        assertThat(clockRepository.findById(1L))
                .isPresent()
                .hasValueSatisfying(e -> assertThat(e.getCurrentDay()).isEqualTo(3));
    }

    @Test
    @DisplayName("POST /tick/{days} - el publisher se invoca exactamente una vez por tick")
    void advanceTime_publisherCalledOnce() throws Exception {
        mockMvc.perform(post("/tick/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(timeAdvancedEventPublisher, times(1)).publish(any());
    }

    @Test
    @DisplayName("POST /tick/{days} - dos ticks consecutivos acumulan el día correctamente")
    void advanceTime_twoConsecutiveTicks_accumulatesDays() throws Exception {
        mockMvc.perform(post("/tick/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentDay").value(2));

        mockMvc.perform(post("/tick/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previousDay").value(2))
                .andExpect(jsonPath("$.currentDay").value(5))
                .andExpect(jsonPath("$.daysAdvanced").value(3));

        assertThat(clockRepository.findById(1L))
                .isPresent()
                .hasValueSatisfying(e -> assertThat(e.getCurrentDay()).isEqualTo(5));

        verify(timeAdvancedEventPublisher, times(2)).publish(any());
    }

    @Test
    @DisplayName("POST /tick/0 - días = 0 devuelve 416")
    void advanceTime_zeroDays_returns416() throws Exception {
        mockMvc.perform(post("/tick/0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isRequestedRangeNotSatisfiable());
    }

    @Test
    @DisplayName("POST /tick/-1 - días negativos devuelve 416")
    void advanceTime_negativeDays_returns416() throws Exception {
        mockMvc.perform(post("/tick/-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isRequestedRangeNotSatisfiable());
    }
}
