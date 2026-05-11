package com.gft.mstime.infraestructure.web;

import com.gft.mstime.application.usecase.AdvanceTimeUseCase;
import com.gft.mstime.application.command.AdvanceTimeCommand;
import com.gft.mstime.application.result.TimeAdvancedResult;
import com.gft.mstime.infraestructure.web.request.AdvanceTimeRequest;
import com.gft.mstime.infraestructure.web.response.TimeAdvancedResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class SimulationClockController {

    private final AdvanceTimeUseCase advanceTimeUseCase;

    public SimulationClockController(AdvanceTimeUseCase advanceTimeUseCase) {
        this.advanceTimeUseCase = Objects.requireNonNull(advanceTimeUseCase, "advanceTimeUseCase cannot be null");
    }

    @PostMapping("/tick")
    public ResponseEntity<TimeAdvancedResponse> advanceTime(@Valid @RequestBody AdvanceTimeRequest request) {
        TimeAdvancedResult result = advanceTimeUseCase.advanceTime(new AdvanceTimeCommand(request.days()));

        return ResponseEntity.ok(TimeAdvancedResponse.from(result));
    }
}
