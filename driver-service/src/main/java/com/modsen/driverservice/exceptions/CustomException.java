package com.modsen.driverservice.exceptions;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{
    String field;

    public CustomException(String field, String message) {
        super(message);
        this.field = field;
    }
}
