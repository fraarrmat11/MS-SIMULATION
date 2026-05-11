package com.gft.mstime.infraestructure.web;

import com.gft.mstime.application.usecase.AdvanceTimeUseCase;
import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.mstime.infraestructure.web.response.CurrentSimulationDayResponse;
import com.gft.mstime.infraestructure.web.response.TimeAdvancedResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SimulationClockControllerTest {

    @Test
    void advanceTime_WhenRequestIsValid_ShouldAdvanceTimeAndReturnTimeAdvancedResponse() {
        AdvanceTimeUseCase advanceTimeUseCase = mock(AdvanceTimeUseCase.class);
        GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase = mock(GetCurrentSimulationDayUseCase.class);
        SimulationClockController controller = new SimulationClockController(
                advanceTimeUseCase,
                getCurrentSimulationDayUseCase
        );
        UUID eventId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Instant occurredAt = Instant.parse("2026-05-04T11:30:00Z");
        TimeAdvancedResult result = new TimeAdvancedResult(eventId, 2, 5, 3, occurredAt);
        when(advanceTimeUseCase.advanceTime(any(AdvanceTimeCommand.class))).thenReturn(result);

        ResponseEntity<TimeAdvancedResponse> response = controller.advanceTime(3);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().eventId()).isEqualTo(eventId);
        assertThat(response.getBody().previousDay()).isEqualTo(2);
        assertThat(response.getBody().currentDay()).isEqualTo(5);
        assertThat(response.getBody().daysAdvanced()).isEqualTo(3);
        assertThat(response.getBody().occurredAt()).isEqualTo(occurredAt);

        ArgumentCaptor<AdvanceTimeCommand> commandCaptor = ArgumentCaptor.forClass(AdvanceTimeCommand.class);
        verify(advanceTimeUseCase).advanceTime(commandCaptor.capture());
        assertThat(commandCaptor.getValue().days()).isEqualTo(3);
        verifyNoMoreInteractions(advanceTimeUseCase);
        verifyNoMoreInteractions(getCurrentSimulationDayUseCase);
    }

    @Test
    void advanceTime_WhenDaysIsLessThanOne_ShouldReturnRequestedRangeNotSatisfiable() {
        AdvanceTimeUseCase advanceTimeUseCase = mock(AdvanceTimeUseCase.class);
        GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase = mock(GetCurrentSimulationDayUseCase.class);
        SimulationClockController controller = new SimulationClockController(
                advanceTimeUseCase,
                getCurrentSimulationDayUseCase
        );

        ResponseEntity<TimeAdvancedResponse> response = controller.advanceTime(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        assertThat(response.getBody()).isNull();
        verifyNoMoreInteractions(advanceTimeUseCase, getCurrentSimulationDayUseCase);
    }

    @Test
    void getCurrentSimulationDay_WhenRequested_ShouldReturnCurrentDayResponse() {
        AdvanceTimeUseCase advanceTimeUseCase = mock(AdvanceTimeUseCase.class);
        GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase = mock(GetCurrentSimulationDayUseCase.class);
        SimulationClockController controller = new SimulationClockController(
                advanceTimeUseCase,
                getCurrentSimulationDayUseCase
        );
        when(getCurrentSimulationDayUseCase.getCurrentSimulationDay()).thenReturn(6);

        ResponseEntity<CurrentSimulationDayResponse> response = controller.getCurrentSimulationDay();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().currentDay()).isEqualTo(6);
        verify(getCurrentSimulationDayUseCase).getCurrentSimulationDay();
        verifyNoMoreInteractions(advanceTimeUseCase, getCurrentSimulationDayUseCase);
    }

    @Test
    void constructor_WhenAdvanceTimeUseCaseIsNull_ShouldThrowException() {
        GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase = mock(GetCurrentSimulationDayUseCase.class);

        assertThatThrownBy(() -> new SimulationClockController(null, getCurrentSimulationDayUseCase))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("advanceTimeUseCase cannot be null");
    }

    @Test
    void constructor_WhenGetCurrentSimulationDayUseCaseIsNull_ShouldThrowException() {
        AdvanceTimeUseCase advanceTimeUseCase = mock(AdvanceTimeUseCase.class);

        assertThatThrownBy(() -> new SimulationClockController(advanceTimeUseCase, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("getCurrentSimulationDayUseCase cannot be null");
    }
}
