package com.example.demo0424.exception;

public class GenderMismatchException extends RuntimeException {
    public GenderMismatchException(String message) {
        super(message);
    }
}
