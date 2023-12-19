package com.modsen.driverservice.exceptions;

public class NotFoundException extends CustomException {
    public NotFoundException(String field, String message) {
        super(field, message);
    }
}
