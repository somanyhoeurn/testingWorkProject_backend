package com.example.testproject.exceptions;

public class CustomerHasActiveException extends RuntimeException {
    public CustomerHasActiveException(String message) {
        super(message);
    }
}
