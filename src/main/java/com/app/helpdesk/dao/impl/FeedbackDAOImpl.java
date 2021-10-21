package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.FeedbackDAO;
import com.app.helpdesk.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class FeedbackDAOImpl implements FeedbackDAO {

    private final EntityManager entityManager;

    @Autowired
    public FeedbackDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        entityManager.persist(feedback);
    }

    @Override
    public List<Feedback> getFeedbacks(Long userId, Long ticketId) {
        return entityManager.createQuery("from Feedback where ticket.id = :ticketId and user.id = :userId", Feedback.class)
                .setParameter("ticketId", ticketId)
                .setParameter("userId", userId)
                .setMaxResults(10)
                .getResultList();
    }
}
