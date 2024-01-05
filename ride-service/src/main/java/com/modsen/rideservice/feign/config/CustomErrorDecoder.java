package com.modsen.rideservice.feign.config;

import com.modsen.rideservice.exceptions.BalanceException;
import com.modsen.rideservice.exceptions.NotFoundException;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = FeignException.errorStatus(methodKey, response);
        int status = response.status();
        if (status >= 500) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Long) null,
                    response.request());
        } else if (status == 404) {
            return new NotFoundException("not-found", exception.contentUTF8());
        } else if (status == 400) {
            return new BalanceException("balance", exception.contentUTF8());
        }
        return exception;
    }
}
