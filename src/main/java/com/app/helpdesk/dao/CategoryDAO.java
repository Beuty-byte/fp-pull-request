package com.app.helpdesk.dao;

import com.app.helpdesk.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO {
    List<Category> getAllCategory();

    Optional<Category> getCategoryByName(String categoryName);
}
