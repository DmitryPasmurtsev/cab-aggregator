package com.modsen.passengerservice.exceptions.handler;

import com.modsen.passengerservice.exceptions.CustomException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class PassengerExceptionHandler {
    static final String FIRST_KEY = "cause";
    static final String SECOND_KEY = "message";

    @ExceptionHandler(value = {NotFoundException.class, NotCreatedException.class})
    public ResponseEntity<ExceptionResponse> handleNotCreatedException(CustomException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(FIRST_KEY, ex.getField());
        error.put(SECOND_KEY, ex.getMessage());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(error);
        return new ResponseEntity<>(new ExceptionResponse(list), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PropertyReferenceException.class})
    public ResponseEntity<ExceptionResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(FIRST_KEY, ex.getPropertyName());
        error.put(SECOND_KEY, ex.getMessage());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(error);
        return new ResponseEntity<>(new ExceptionResponse(list), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(FIRST_KEY, "pagination");
        error.put(SECOND_KEY, ex.getMessage());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(error);
        return new ResponseEntity<>(new ExceptionResponse(list), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
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
}
