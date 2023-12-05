package com.example.autodeal.exception;

public class OrderLineValidationException extends RuntimeException {


    public OrderLineValidationException(String message) {
        super(message);
    }

}