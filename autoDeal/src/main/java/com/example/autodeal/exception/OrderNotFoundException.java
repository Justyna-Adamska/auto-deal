package com.example.autodeal.exception;

public class OrderNotFoundException extends RuntimeException {


    public OrderNotFoundException(String message) {
        super(message);
    }
}
