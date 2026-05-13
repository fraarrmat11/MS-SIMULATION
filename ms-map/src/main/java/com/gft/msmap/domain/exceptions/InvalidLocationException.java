package com.gft.msmap.domain.exceptions;

public class InvalidLocationException extends RuntimeException {
    public InvalidLocationException(int x, int y) {
        super("Location coordinates can't be negative, got: x=" + x + ",y=" + y);
    }
}
