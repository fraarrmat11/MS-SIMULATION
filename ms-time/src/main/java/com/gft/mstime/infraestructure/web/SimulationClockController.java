package com.gft.mstime.infraestructure.web;

import com.gft.mstime.application.usecase.AdvanceTimeUseCase;
import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.application.usecase.GetCurrentSimulationDayUseCase;
import com.gft.mstime.infraestructure.web.response.CurrentSimulationDayResponse;
import com.gft.mstime.infraestructure.web.response.TimeAdvancedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        int currentDay = getCurrentSimulationDayUseCase.getCurrentSimulationDay();

        return ResponseEntity.ok(new CurrentSimulationDayResponse(currentDay));
    }

    @PostMapping("{days}")
    @Operation(summary = "Post to advance X days")
    public ResponseEntity<TimeAdvancedResponse> advanceTime(@Valid @PathVariable int days) {
        if(days < 1){
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }
        TimeAdvancedResult result = advanceTimeUseCase.advanceTime(new AdvanceTimeCommand(days));
        return ResponseEntity.ok(TimeAdvancedResponse.from(result));
    }
}
