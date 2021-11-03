package com.app.helpdesk.exception;

public class NoAccessToChangeTicketState extends RuntimeException{
    public NoAccessToChangeTicketState(String message) {
        super(message);
    }
}
