package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.AttachmentDAO;
import com.app.helpdesk.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    private final EntityManager entityManager;

    @Autowired
    public AttachmentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Attachment> getAttachmentById(Long attachmentId) {
        return entityManager.createQuery("from Attachment where id = :id", Attachment.class)
                .setParameter("id", attachmentId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void deleteAttachment(Attachment attachment) {
        entityManager.remove(attachment);
    }

    @Override
    public Optional<Attachment> checkAccessToAttachment(Long userId, Long attachmentId) {
        return entityManager.createQuery("from Attachment where id = :id and ticket.owner.id = :userId", Attachment.class)
                .setParameter("id", attachmentId)
                .setParameter("userId", userId)
                .getResultStream()
                .findAny();
    }
}
