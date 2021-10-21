package com.app.helpdesk.exception;

public class NotFoundAttachmentException extends RuntimeException{
    public NotFoundAttachmentException(String message) {
        super(message);
    }
}
