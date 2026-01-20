package com.example.e_commerce1.exception;

public class EmptyCartException extends RuntimeException {
    
    public EmptyCartException(String message) {
        super(message);
    }
}
