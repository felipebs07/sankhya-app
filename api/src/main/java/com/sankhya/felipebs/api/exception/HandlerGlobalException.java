package com.sankhya.felipebs.api.exception;

import com.sankhya.felipebs.api.exception.custom.ConcurrencyConflictException;
import com.sankhya.felipebs.api.exception.custom.EntityNotFoundException;
import com.sankhya.felipebs.api.exception.custom.InsufficientQuantityProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerGlobalException {

    @ExceptionHandler(InsufficientQuantityProductException.class)
    public ResponseEntity<String> handleIsufficientQuantityProductException(InsufficientQuantityProductException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConcurrencyConflictException.class)
    public ResponseEntity<String> handleConcurrencyConflictException(ConcurrencyConflictException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
