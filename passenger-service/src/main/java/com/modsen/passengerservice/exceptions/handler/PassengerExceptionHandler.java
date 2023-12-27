package com.modsen.passengerservice.exceptions.handler;

import com.modsen.passengerservice.exceptions.NotCreatedException;
import com.modsen.passengerservice.exceptions.NotFoundException;
import com.modsen.passengerservice.exceptions.response.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class PassengerExceptionHandler {
    private static final String EXCEPTION_CAUSE_KEY = "cause";
    private static final String EXCEPTION_MESSAGE_KEY = "message";

    private final Locale locale;
    private final MessageSource messageSource;

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createResponse(ex.getField(), ex.getMessage()));
    }

    @ExceptionHandler(value = {NotCreatedException.class})
    public ResponseEntity<ExceptionResponse> handleNotCreatedException(NotCreatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createResponse(ex.getField(), ex.getMessage()));
    }

    @ExceptionHandler(value = {PropertyReferenceException.class})
    public ResponseEntity<ExceptionResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createResponse(ex.getPropertyName(), ex.getMessage()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createResponse("pagination", ex.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> responseList = ex.getFieldErrors().stream()
                .map(err -> Map.of(EXCEPTION_CAUSE_KEY, err.getField(), EXCEPTION_MESSAGE_KEY, err.getDefaultMessage()))
                .toList();
        ExceptionResponse response = new ExceptionResponse(responseList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    private ExceptionResponse createResponse(String cause, String message) {
        List<Map<String, String>> list = Collections.singletonList(Map.of(EXCEPTION_CAUSE_KEY, cause, EXCEPTION_MESSAGE_KEY, messageSource.getMessage(message, null, locale)));
        return new ExceptionResponse(list);
    }
}
