package com.gft.mstime.infraestructure.web;

import com.gft.mstime.application.usecase.AdvanceTimeUseCase;
import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.domain.SimulationDay;
import com.gft.mstime.infraestructure.web.response.TimeAdvancedResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class SimulationClockController {

    private final AdvanceTimeUseCase advanceTimeUseCase;

    public SimulationClockController(AdvanceTimeUseCase advanceTimeUseCase) {
        this.advanceTimeUseCase = Objects.requireNonNull(advanceTimeUseCase, "advanceTimeUseCase cannot be null");
    }

    @PostMapping("/tick/{days}")
    public ResponseEntity<TimeAdvancedResponse> advanceTime(@Valid @PathVariable int days) {
        if(days < 1){
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }
        TimeAdvancedResult result = advanceTimeUseCase.advanceTime(new AdvanceTimeCommand(days));
        return ResponseEntity.ok(TimeAdvancedResponse.from(result));
    }
}
