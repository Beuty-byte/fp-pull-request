package com.app.helpdesk.exception;

public class NotFoundStateException extends RuntimeException{
    public NotFoundStateException(String message) {
        super(message);
    }
}
