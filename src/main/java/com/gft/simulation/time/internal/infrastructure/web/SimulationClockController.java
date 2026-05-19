package com.gft.simulation.time.internal.infrastructure.web;

import com.gft.simulation.time.internal.application.command.AdvanceTimeCommand;
import com.gft.simulation.time.internal.application.result.TimeAdvancedResult;
import com.gft.simulation.time.internal.application.usecase.AdvanceTimeUseCase;
import com.gft.simulation.time.internal.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.simulation.time.internal.infrastructure.web.response.CurrentSimulationDayResponse;
import com.gft.simulation.time.internal.infrastructure.web.response.TimeAdvancedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RequestMapping("tick")
@RestController
@Tag(name = "Time", description = "API for time operations")
public class SimulationClockController {

    private final AdvanceTimeUseCase advanceTimeUseCase;
    private final GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase;

    public SimulationClockController(
            AdvanceTimeUseCase advanceTimeUseCase,
            GetCurrentSimulationDayUseCase getCurrentSimulationDayUseCase
    ) {
        this.advanceTimeUseCase = Objects.requireNonNull(advanceTimeUseCase, "advanceTimeUseCase cannot be null");
        this.getCurrentSimulationDayUseCase = Objects.requireNonNull(
                getCurrentSimulationDayUseCase,
                "getCurrentSimulationDayUseCase cannot be null"
        );
    }

    @GetMapping("current")
    @Operation(summary = "Getter to know the current day")
    public ResponseEntity<CurrentSimulationDayResponse> getCurrentSimulationDay() {
        log.info("HTTP GET /tick/current received");
        int currentDay = getCurrentSimulationDayUseCase.getCurrentSimulationDay();
        log.info("HTTP GET /tick/current completed: status=200, currentDay={}", currentDay);
        return ResponseEntity.ok(new CurrentSimulationDayResponse(currentDay));
    }

    @PostMapping("{days}")
    @Operation(summary = "Post to advance X days")
    public ResponseEntity<TimeAdvancedResponse> advanceTime(@Valid @PathVariable int days) {
        log.info("HTTP POST /tick/{} received", days);

        TimeAdvancedResult result = advanceTimeUseCase.advanceTime(new AdvanceTimeCommand(days));
        log.info(
                "HTTP POST /tick/{} completed: status=200, previousDay={}, currentDay={}, eventId={}",
                days,
                result.previousDay(),
                result.currentDay(),
                result.eventId()
        );
        return ResponseEntity.ok(TimeAdvancedResponse.from(result));
    }
}
