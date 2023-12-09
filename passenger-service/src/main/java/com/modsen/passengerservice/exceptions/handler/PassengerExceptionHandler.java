package com.modsen.passengerservice.exceptions.handler;

import com.modsen.passengerservice.exceptions.NotCreatedException;
import com.modsen.passengerservice.exceptions.NotFoundException;
import com.modsen.passengerservice.exceptions.response.ExceptionResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PassengerExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException){
        ExceptionResponse response = new ExceptionResponse(notFoundException.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = {NotCreatedException.class})
    public ResponseEntity<Object> handleNotCreatedException(NotCreatedException notCreatedException){
        ExceptionResponse response = new ExceptionResponse(notCreatedException.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {PropertyReferenceException.class})
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException propertyReferenceException){
        ExceptionResponse response = new ExceptionResponse(propertyReferenceException.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException){
        ExceptionResponse response = new ExceptionResponse(illegalArgumentException.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
