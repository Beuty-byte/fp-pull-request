package com.app.helpdesk.dao;

import com.app.helpdesk.model.Attachment;

import java.util.Optional;

public interface AttachmentDAO {
    Optional<Attachment> getAttachmentById(Long attachmentId);

    void deleteAttachment(Attachment attachment);

    Optional<Attachment> checkAccessToAttachment(Long userId, Long attachmentId);
}
