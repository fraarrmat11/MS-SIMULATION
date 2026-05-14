package com.gft.mstime.domain.exceptions;

public class InvalidDaysToAdvanceException extends RuntimeException {
    public InvalidDaysToAdvanceException(int daysToAdvance) {
        super("Days to advance must be greater than zero, got: " + daysToAdvance);
    }
}
