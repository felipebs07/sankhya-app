package com.sankhya.felipebs.api.exception.custom;

public class InsufficientQuantityProductException extends RuntimeException {
    public InsufficientQuantityProductException(String message) {
        super(message);
    }
}
