package com.app.helpdesk.exception;

public class NotAccessToTicketException extends RuntimeException{
    public NotAccessToTicketException(String message) {
        super(message);
    }
}
