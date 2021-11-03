package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.UserDAO;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
    private final EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Session session = entityManager.unwrap(Session.class);
        User user = session.createQuery("from User where email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAllManagers() {
        return entityManager.createQuery("from User where role = 'ROLE_MANAGER'", User.class)
                .getResultList();
    }

    @Override
    public User getCreatorTicket(Long ticketId) {
        return entityManager.createQuery("from User u JOIN FETCH u.ticketOwned t where t.id = :ticketId", User.class)
                .setParameter("ticketId", ticketId)
                .getSingleResult();
    }

    @Override
    public List<User> getAllEngineer() {
        return entityManager.createQuery("from User where role = 'ROLE_ENGINEER'", User.class)
                .getResultList();
    }

    @Override
    public User getApproverTicket(Long ticketId) {
        return entityManager.createQuery("from User u JOIN FETCH u.approved t where t.id = :ticketId", User.class)
                .setParameter("ticketId", ticketId)
                .getSingleResult();
    }

    @Override
    public User getEngineerForFeedback(Long ticketId) {
        return entityManager.createQuery("from Ticket where id = :ticketId", Ticket.class)
                .setParameter("ticketId", ticketId)
                .getSingleResult()
                .getAssignee();
    }

}
