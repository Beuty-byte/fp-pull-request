package com.app.helpdesk.exception;

public class NotAccessToAttachmentDelete extends RuntimeException{
    public NotAccessToAttachmentDelete(String message) {
        super(message);
    }
}
