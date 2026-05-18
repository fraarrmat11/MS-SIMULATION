package com.gft.simulation.clock.internal.infrastructure.web;

import com.gft.simulation.clock.internal.application.usecase.AdvanceTimeUseCase;
import com.gft.simulation.clock.internal.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.simulation.clock.internal.domain.SimulationDay;
import com.gft.simulation.clock.internal.domain.exceptions.InvalidDaysToAdvanceException;
import com.gft.simulation.clock.internal.domain.exceptions.InvalidSimulationDayException;
import com.gft.simulation.clock.internal.domain.exceptions.InvalidTimeAdvanceException;
import com.gft.simulation.clock.internal.infrastructure.persistence.jpa.exceptions.InvalidCurrentDayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private final AdvanceTimeUseCase advanceTimeUseCase = mock(AdvanceTimeUseCase.class);
    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase =
            mock(GetCurrentSimulationDayUseCase.class);
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new SimulationClockController(advanceTimeUseCase, getCurrentSimulationDayUseCase))
                .setControllerAdvice(new ClockGlobalExceptionHandler())
                .build();
    }

    @Test
    void handleInvalidDaysToAdvance_ShouldReturn422() throws Exception {
        when(advanceTimeUseCase.advanceTime(any())).thenThrow(new InvalidDaysToAdvanceException(0));

        mockMvc.perform(post("/tick/1")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void handleInvalidSimulationDay_ShouldReturn422() throws Exception {
        when(advanceTimeUseCase.advanceTime(any())).thenThrow(new InvalidSimulationDayException(-1));

        mockMvc.perform(post("/tick/1")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void handleInvalidTimeAdvance_ShouldReturn409() throws Exception {
        when(advanceTimeUseCase.advanceTime(any())).thenThrow(
                new InvalidTimeAdvanceException(SimulationDay.fromDayNumber(5), SimulationDay.fromDayNumber(3)));

        mockMvc.perform(post("/tick/1")).andExpect(status().isConflict());
    }

    @Test
    void handleInvalidCurrentDay_ShouldReturn500() throws Exception {
        when(advanceTimeUseCase.advanceTime(any())).thenThrow(new InvalidCurrentDayException(-1));

        mockMvc.perform(post("/tick/1")).andExpect(status().isInternalServerError());
    }

    @Test
    void handleUnexpected_ShouldReturn500() throws Exception {
        when(advanceTimeUseCase.advanceTime(any())).thenThrow(new RuntimeException("unexpected"));

        mockMvc.perform(post("/tick/1")).andExpect(status().isInternalServerError());
    }
}
