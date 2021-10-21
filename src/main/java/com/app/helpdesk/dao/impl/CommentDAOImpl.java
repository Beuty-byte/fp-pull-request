package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.CommentDAO;
import com.app.helpdesk.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CommentDAOImpl implements CommentDAO {

    private final EntityManager entityManager;

    @Autowired
    public CommentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveComment(Comment comment) {
        entityManager.persist(comment);
    }
}
