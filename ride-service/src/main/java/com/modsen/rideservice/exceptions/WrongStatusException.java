package com.modsen.rideservice.exceptions;

public class WrongStatusException extends CustomException {
    public WrongStatusException(String field, String message) {
        super(field, message);
    }
}
