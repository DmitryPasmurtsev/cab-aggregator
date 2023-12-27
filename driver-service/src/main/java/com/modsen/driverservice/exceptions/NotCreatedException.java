package com.modsen.driverservice.exceptions;

public class NotCreatedException extends CustomException {
    public NotCreatedException(String field, String message) {
        super(field, message);
    }
}
