package com.modsen.passengerservice.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class CustomException extends RuntimeException{
    final String field;

    public CustomException(String field, String message) {
        super(message);
        this.field = field;
    }
}
