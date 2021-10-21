package com.app.helpdesk.exception;

public class NotAccessToChangeTicketState extends RuntimeException{
    public NotAccessToChangeTicketState(String message) {
        super(message);
    }
}
