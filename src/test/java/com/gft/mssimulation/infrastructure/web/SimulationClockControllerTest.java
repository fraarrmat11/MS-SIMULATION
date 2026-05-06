package com.gft.mssimulation.infrastructure.web;

import com.gft.mssimulation.application.simulationclock.AdvanceTimeUseCase;
import com.gft.mssimulation.application.simulationclock.command.AdvanceTimeCommand;
import com.gft.mssimulation.application.simulationclock.result.TimeAdvancedResult;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulationClockController.class)
@Import(RestExceptionHandler.class)
class SimulationClockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdvanceTimeUseCase advanceTimeUseCase;

    @Test
    void advanceTime_WhenRequestIsValid_ShouldAdvanceTimeAndReturnTimeAdvancedResponse() throws Exception {
        UUID eventId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Instant occurredAt = Instant.parse("2026-05-04T11:30:00Z");
        TimeAdvancedResult result = new TimeAdvancedResult(eventId, 2, 5, 3, occurredAt);
        when(advanceTimeUseCase.advanceTime(any(AdvanceTimeCommand.class))).thenReturn(result);

        mockMvc.perform(post("/tick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"days\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId.toString()))
                .andExpect(jsonPath("$.previousDay").value(2))
                .andExpect(jsonPath("$.currentDay").value(5))
                .andExpect(jsonPath("$.daysAdvanced").value(3))
                .andExpect(jsonPath("$.occurredAt").value("2026-05-04T11:30:00Z"));

        ArgumentCaptor<AdvanceTimeCommand> commandCaptor = ArgumentCaptor.forClass(AdvanceTimeCommand.class);
        verify(advanceTimeUseCase).advanceTime(commandCaptor.capture());
        assertThat(commandCaptor.getValue().days()).isEqualTo(3);
        verifyNoMoreInteractions(advanceTimeUseCase);
    }

    @Test
    void advanceTime_WhenDaysIsZero_ShouldReturnBadRequestAndNotCallUseCase() throws Exception {
        mockMvc.perform(post("/tick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"days\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid request"))
                .andExpect(jsonPath("$.errors[0]").value(containsString("days")));

        verifyNoInteractions(advanceTimeUseCase);
    }

    @Test
    void advanceTime_WhenDaysIsNegative_ShouldReturnBadRequestAndNotCallUseCase() throws Exception {
        mockMvc.perform(post("/tick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"days\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid request"))
                .andExpect(jsonPath("$.errors[0]").value(containsString("days")));

        verifyNoInteractions(advanceTimeUseCase);
    }

    @Test
    void advanceTime_WhenDaysIsMissing_ShouldReturnBadRequestAndNotCallUseCase() throws Exception {
        mockMvc.perform(post("/tick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid request"))
                .andExpect(jsonPath("$.errors[0]").value(containsString("days")));

        verifyNoInteractions(advanceTimeUseCase);
    }

    @Test
    void advanceTime_WhenJsonIsMalformed_ShouldReturnBadRequestAndNotCallUseCase() throws Exception {
        mockMvc.perform(post("/tick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"days\":}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Malformed JSON request"));

        verifyNoInteractions(advanceTimeUseCase);
    }

    @Test
    void advanceTime_WhenUseCaseRejectsCommand_ShouldReturnBadRequest() throws Exception {
        when(advanceTimeUseCase.advanceTime(any(AdvanceTimeCommand.class)))
                .thenThrow(new IllegalArgumentException("Days to advance must be greater than zero"));

        mockMvc.perform(post("/tick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"days\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Days to advance must be greater than zero"));

        ArgumentCaptor<AdvanceTimeCommand> commandCaptor = ArgumentCaptor.forClass(AdvanceTimeCommand.class);
        verify(advanceTimeUseCase).advanceTime(commandCaptor.capture());
        assertThat(commandCaptor.getValue().days()).isEqualTo(1);
        verifyNoMoreInteractions(advanceTimeUseCase);
    }
}
