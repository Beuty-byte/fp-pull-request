package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.UserDAO;
import com.app.helpdesk.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
