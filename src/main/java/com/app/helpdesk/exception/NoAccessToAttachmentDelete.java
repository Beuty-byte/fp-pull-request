package com.app.helpdesk.exception;

public class NoAccessToAttachmentDelete extends RuntimeException{
    public NoAccessToAttachmentDelete(String message) {
        super(message);
    }
}
