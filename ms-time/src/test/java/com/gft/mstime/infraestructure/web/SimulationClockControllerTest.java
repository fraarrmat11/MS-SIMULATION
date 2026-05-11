package com.gft.mstime.infraestructure.web;

import com.gft.mstime.application.usecase.AdvanceTimeUseCase;
import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.mstime.infraestructure.web.response.TimeAdvancedResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SimulationClockControllerTest {

    private static final UUID EVENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final Instant OCCURRED_AT = Instant.parse("2026-05-04T11:30:00Z");
    private static final TimeAdvancedResult TIME_ADVANCED_RESULT =
            new TimeAdvancedResult(EVENT_ID, 2, 5, 3, OCCURRED_AT);

    private final AdvanceTimeUseCase advanceTimeUseCase = mock(AdvanceTimeUseCase.class);
    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase =
            mock(GetCurrentSimulationDayUseCase.class);
    private final SimulationClockController controller = new SimulationClockController(
            advanceTimeUseCase,
            getCurrentSimulationDayUseCase
    );
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    @Test
    void advanceTime_WhenRequestIsValid_ShouldAdvanceTimeAndReturnTimeAdvancedResponse() {
        when(advanceTimeUseCase.advanceTime(any(AdvanceTimeCommand.class))).thenReturn(TIME_ADVANCED_RESULT);

        ResponseEntity<TimeAdvancedResponse> response = controller.advanceTime(3);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().eventId()).isEqualTo(EVENT_ID);
        assertThat(response.getBody().previousDay()).isEqualTo(2);
        assertThat(response.getBody().currentDay()).isEqualTo(5);
        assertThat(response.getBody().daysAdvanced()).isEqualTo(3);
        assertThat(response.getBody().occurredAt()).isEqualTo(OCCURRED_AT);

        ArgumentCaptor<AdvanceTimeCommand> commandCaptor = ArgumentCaptor.forClass(AdvanceTimeCommand.class);
        verify(advanceTimeUseCase).advanceTime(commandCaptor.capture());
        assertThat(commandCaptor.getValue().days()).isEqualTo(3);
        verifyNoMoreInteractions(advanceTimeUseCase);
        verifyNoMoreInteractions(getCurrentSimulationDayUseCase);
    }

    @Test
    void advanceTime_WhenDaysIsLessThanOne_ShouldReturnRequestedRangeNotSatisfiable() {
        ResponseEntity<TimeAdvancedResponse> response = controller.advanceTime(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        assertThat(response.getBody()).isNull();
        verifyNoMoreInteractions(advanceTimeUseCase, getCurrentSimulationDayUseCase);
    }

    @Test
    void getCurrentSimulationDay_WhenCalledThroughHttpMapping_ShouldExposeTickCurrentEndpoint() throws Exception {
        when(getCurrentSimulationDayUseCase.getCurrentSimulationDay()).thenReturn(6);

        mockMvc.perform(get("/tick/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentDay").value(6));

        verify(getCurrentSimulationDayUseCase).getCurrentSimulationDay();
        verifyNoMoreInteractions(advanceTimeUseCase, getCurrentSimulationDayUseCase);
    }

    @Test
    void advanceTime_WhenCalledThroughHttpMapping_ShouldKeepTickDaysEndpoint() throws Exception {
        when(advanceTimeUseCase.advanceTime(any(AdvanceTimeCommand.class))).thenReturn(TIME_ADVANCED_RESULT);

        mockMvc.perform(post("/tick/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(EVENT_ID.toString()))
                .andExpect(jsonPath("$.previousDay").value(2))
                .andExpect(jsonPath("$.currentDay").value(5))
                .andExpect(jsonPath("$.daysAdvanced").value(3));

        verify(advanceTimeUseCase).advanceTime(any(AdvanceTimeCommand.class));
        verifyNoMoreInteractions(advanceTimeUseCase, getCurrentSimulationDayUseCase);
    }

    @Test
    void constructor_WhenAdvanceTimeUseCaseIsNull_ShouldThrowException() {
        assertThatThrownBy(() -> new SimulationClockController(null, getCurrentSimulationDayUseCase))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("advanceTimeUseCase cannot be null");
    }

    @Test
    void constructor_WhenGetCurrentSimulationDayUseCaseIsNull_ShouldThrowException() {
        assertThatThrownBy(() -> new SimulationClockController(advanceTimeUseCase, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("getCurrentSimulationDayUseCase cannot be null");
    }
}
