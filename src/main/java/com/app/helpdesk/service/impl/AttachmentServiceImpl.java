package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.AttachmentDAO;
import com.app.helpdesk.exception.AttachmentNotFoundException;
import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentDAO attachmentDAO;

    @Autowired
    public AttachmentServiceImpl(AttachmentDAO attachmentDAO) {
        this.attachmentDAO = attachmentDAO;
    }

    @Override
    public Attachment getAttachmentById(Long attachmentId) {
        return attachmentDAO.getAttachmentById(attachmentId)
                .orElseThrow(() -> new AttachmentNotFoundException(String.format("Attachment with id : %s not found", attachmentId)));
    }

    @Override
    public void deleteAttachmentById(Long attachmentId) {
        Attachment attachment = getAttachmentById(attachmentId);
        attachmentDAO.deleteAttachment(attachment);
    }
}
