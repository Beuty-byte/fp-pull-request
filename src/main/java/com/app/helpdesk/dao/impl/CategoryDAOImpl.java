package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.CategoryDAO;
import com.app.helpdesk.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDAOImpl implements CategoryDAO {

    private final EntityManager entityManager;

    @Autowired
    public CategoryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Category> getAllCategory() {
        return entityManager.createQuery("from Category", Category.class).getResultList();
    }

    @Override
    public Optional<Category> getCategoryByName(String categoryName) {
        return entityManager.createQuery("from Category where name = :name", Category.class)
                .setParameter("name", categoryName)
                .getResultStream()
                .findAny();
    }
}
