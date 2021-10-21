package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.HistoryDAO;
import com.app.helpdesk.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class HistoryDAOImpl implements HistoryDAO {

    private final EntityManager entityManager;

    @Autowired
    public HistoryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveHistory(History history) {
        entityManager.persist(history);
    }
}
