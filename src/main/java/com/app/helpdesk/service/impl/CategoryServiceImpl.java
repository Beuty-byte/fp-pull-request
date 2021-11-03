package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.CategoryDAO;
import com.app.helpdesk.dto.CategoryDto;
import com.app.helpdesk.exception.NoSuchCategoryException;
import com.app.helpdesk.model.Category;
import com.app.helpdesk.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedHashSet;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;

    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public CategoryDto getAllCategoryWithUrgency() {
        return new CategoryDto(new LinkedHashSet<>(categoryDAO.getAllCategory()));
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryDAO.getCategoryByName(categoryName)
                .orElseThrow(() -> new NoSuchCategoryException(String.format("Category with name: %s not found"
                        , categoryName)));
    }
}
