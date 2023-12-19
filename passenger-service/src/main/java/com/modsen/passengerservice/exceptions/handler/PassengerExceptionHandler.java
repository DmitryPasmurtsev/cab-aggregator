package com.modsen.passengerservice.exceptions.handler;

import com.modsen.passengerservice.exceptions.NotCreatedException;
import com.modsen.passengerservice.exceptions.NotFoundException;
import com.modsen.passengerservice.exceptions.response.ExceptionResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class PassengerExceptionHandler {
    private static final String FIRST_KEY = "cause";
    private static final String SECOND_KEY = "message";

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(createResponse(ex.getField(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NotCreatedException.class})
    public ResponseEntity<ExceptionResponse> handleNotCreatedException(NotCreatedException ex) {
        return new ResponseEntity<>(createResponse(ex.getField(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PropertyReferenceException.class})
    public ResponseEntity<ExceptionResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        return new ResponseEntity<>(createResponse(ex.getPropertyName(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(createResponse("pagination", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> responseList = ex.getFieldErrors().stream()
                .map(err -> Map.of(FIRST_KEY, err.getField(), SECOND_KEY, err.getDefaultMessage()))
                .toList();
        ExceptionResponse response = new ExceptionResponse(responseList);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse createResponse(String firstValue, String secondValue) {
        List<Map<String, String>> list = Collections.singletonList(Map.of(FIRST_KEY, firstValue, SECOND_KEY, secondValue));
        return new ExceptionResponse(list);
    }
}
