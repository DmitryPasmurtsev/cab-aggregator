package com.modsen.driverservice.exceptions;

public class NotAvailableDriverException extends CustomException {
    public NotAvailableDriverException(String field, String message) {
        super(field, message);
    }
}
