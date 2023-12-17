package com.modsen.rideservice.exceptions;


public class NoAccessException extends CustomException {
    public NoAccessException(String field, String message) {
        super(field, message);
    }
}
