package com.gft.simulation.time.internal.infrastructure.web;

import com.gft.simulation.time.internal.domain.exceptions.InvalidDaysToAdvanceException;
import com.gft.simulation.time.internal.domain.exceptions.InvalidSimulationDayException;
import com.gft.simulation.time.internal.domain.exceptions.InvalidTimeAdvanceException;
import com.gft.simulation.time.internal.infrastructure.persistence.jpa.exceptions.InvalidCurrentDayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = SimulationClockController.class)
public class ClockGlobalExceptionHandler {

    @ExceptionHandler(InvalidDaysToAdvanceException.class)
    public ProblemDetail handleInvalidDaysToAdvance(InvalidDaysToAdvanceException ex) {
        log.warn("Invalid days to advance submitted: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(InvalidSimulationDayException.class)
    public ProblemDetail handleInvalidSimulationDay(InvalidSimulationDayException ex) {
        log.warn("Invalid simulation day encountered: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(InvalidTimeAdvanceException.class)
    public ProblemDetail handleInvalidTimeAdvance(InvalidTimeAdvanceException ex) {
        log.warn("Invalid time advance operation: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidCurrentDayException.class)
    public ProblemDetail handleInvalidCurrentDay(InvalidCurrentDayException ex) {
        log.error("Data integrity issue - invalid current day in persistence: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Simulation clock data is corrupted");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
    }
}
