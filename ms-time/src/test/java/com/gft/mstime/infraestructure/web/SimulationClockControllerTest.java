package com.gft.mstime.infraestructure.web;

import com.gft.mstime.application.usecase.AdvanceTimeUseCase;
import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.infraestructure.web.request.AdvanceTimeRequest;
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
        SimulationClockController controller = new SimulationClockController(advanceTimeUseCase);
        UUID eventId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Instant occurredAt = Instant.parse("2026-05-04T11:30:00Z");
        TimeAdvancedResult result = new TimeAdvancedResult(eventId, 2, 5, 3, occurredAt);
        when(advanceTimeUseCase.advanceTime(any(AdvanceTimeCommand.class))).thenReturn(result);

        ResponseEntity<TimeAdvancedResponse> response = controller.advanceTime(new AdvanceTimeRequest(3));

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
    }

    @Test
    void constructor_WhenUseCaseIsNull_ShouldThrowException() {
        assertThatThrownBy(() -> new SimulationClockController(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("advanceTimeUseCase cannot be null");
    }
}
