package com.demo.actors.exception;

public class InternalException extends RuntimeException {

    public InternalException(String message, Exception e) {
        super(message, e);
    }
}
