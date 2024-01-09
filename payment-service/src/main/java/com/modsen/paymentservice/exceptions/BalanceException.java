package com.modsen.paymentservice.exceptions;

public class BalanceException extends CustomException {
    public BalanceException(String field, String message) {
        super(field, message);
    }
}
