package com.app.helpdesk.service;

import com.app.helpdesk.dto.CategoryDto;
import com.app.helpdesk.model.Category;

public interface CategoryService {
    CategoryDto getAllCategoryWithUrgency();

    Category getCategoryByName(String categoryName);
}
