package com.example.currencyconverter.exception;

public class RateNotFoundException extends RuntimeException {
    public RateNotFoundException(final String message) {
        super(message);
    }
}
