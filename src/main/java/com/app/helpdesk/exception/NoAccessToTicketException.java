package com.app.helpdesk.exception;

public class NoAccessToTicketException extends RuntimeException{
    public NoAccessToTicketException(String message) {
        super(message);
    }
}
