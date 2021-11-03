package com.app.helpdesk.service;

import com.app.helpdesk.model.Attachment;

public interface AttachmentService {
    Attachment getAttachmentById(Long attachmentId);
    void deleteAttachmentById(Long attachmentId);
}
