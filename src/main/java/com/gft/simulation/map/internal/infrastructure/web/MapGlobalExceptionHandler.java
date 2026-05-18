package com.gft.simulation.map.internal.infrastructure.web;

import com.gft.simulation.map.internal.domain.exceptions.InvalidLocationException;
import com.gft.simulation.map.internal.domain.exceptions.TruckAlreadyRegisteredException;
import com.gft.simulation.map.internal.domain.exceptions.TruckNotFoundException;
import com.gft.simulation.map.internal.domain.exceptions.WarehouseAlreadyRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MapGlobalExceptionHandler {

    @ExceptionHandler(TruckNotFoundException.class)
    public ProblemDetail handleTruckNotFound(TruckNotFoundException ex) {
        log.warn("Truck not found: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TruckAlreadyRegisteredException.class)
    public ProblemDetail handleTruckAlreadyRegistered(TruckAlreadyRegisteredException ex) {
        log.warn("Truck already registered: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(WarehouseAlreadyRegisteredException.class)
    public ProblemDetail handleWarehouseAlreadyRegistered(WarehouseAlreadyRegisteredException ex) {
        log.warn("Warehouse already registered: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidLocationException.class)
    public ProblemDetail handleInvalidLocation(InvalidLocationException ex) {
        log.warn("Invalid location submitted: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
    }
}
