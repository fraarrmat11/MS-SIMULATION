package com.gft.mstime.infraestructure.persistence.jpa.exceptions;

public class InvalidCurrentDayException extends RuntimeException {
    public InvalidCurrentDayException(int currentDay) {
        super("Current day cannot be negative, got: " + currentDay);
    }
}
