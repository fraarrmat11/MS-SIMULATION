package com.gft.mssimulation.infrastructure.web;

import com.gft.mssimulation.application.simulationclock.AdvanceTimeUseCase;
import com.gft.mssimulation.application.simulationclock.command.AdvanceTimeCommand;
import com.gft.mssimulation.application.simulationclock.result.TimeAdvancedResult;
import com.gft.mssimulation.infrastructure.web.request.AdvanceTimeRequest;
import com.gft.mssimulation.infrastructure.web.response.TimeAdvancedResponse;
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
