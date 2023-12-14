package com.modsen.rideservice.exceptions.handler;

import com.modsen.driverservice.exceptions.CustomException;
import com.modsen.driverservice.exceptions.NotCreatedException;
import com.modsen.driverservice.exceptions.NotFoundException;
import com.modsen.driverservice.exceptions.response.ExceptionResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class DriverExceptionHandler {
    static final String FIRST_KEY = "cause";
    static final String SECOND_KEY = "message";

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

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(createResponse("pagination", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> responseList = new ArrayList<>();
        Map<String, String> responseMap = new HashMap<>();
        ex.getFieldErrors().forEach(err -> {
            responseMap.put(FIRST_KEY, err.getField());
            responseMap.put(SECOND_KEY, err.getDefaultMessage());
            responseList.add(new HashMap<>(responseMap));
        });
        ExceptionResponse response = new ExceptionResponse(responseList);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse createResponse(String firstValue, String secondValue) {
        Map<String, String> error = new HashMap<>();
        error.put(FIRST_KEY, firstValue);
        error.put(SECOND_KEY, secondValue);
        List<Map<String, String>> list = Collections.singletonList(error);
        return new ExceptionResponse(list);
    }
}
