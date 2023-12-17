package com.modsen.paymentservice.exceptions.handler;

import com.modsen.paymentservice.exceptions.CustomException;
import com.modsen.paymentservice.exceptions.NotCreatedException;
import com.modsen.paymentservice.exceptions.NotFoundException;
import com.modsen.paymentservice.exceptions.response.ExceptionResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class RideExceptionHandler {
    private static final String FIRST_KEY = "cause";
    private static final String SECOND_KEY = "message";

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(CustomException ex) {
        return new ResponseEntity<>(createResponse(ex.getField(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotCreatedException.class})
    public ResponseEntity<ExceptionResponse> handleNotCreatedException(CustomException ex) {
        return new ResponseEntity<>(createResponse(ex.getField(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PropertyReferenceException.class})
    public ResponseEntity<ExceptionResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        return new ResponseEntity<>(createResponse(ex.getPropertyName(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> responseList = new ArrayList<>();
        ex.getFieldErrors().forEach(err -> responseList.add(Map.of(FIRST_KEY, err.getField(), SECOND_KEY, err.getDefaultMessage())));
        ExceptionResponse response = new ExceptionResponse(responseList);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse createResponse(String firstValue, String secondValue) {
        List<Map<String, String>> list = Collections.singletonList(Map.of(FIRST_KEY, firstValue, SECOND_KEY, secondValue));
        return new ExceptionResponse(list);
    }
}
