package com.app.helpdesk.exception;

public class AttachmentNotFoundException extends RuntimeException{
    public AttachmentNotFoundException(String message) {
        super(message);
    }
}
