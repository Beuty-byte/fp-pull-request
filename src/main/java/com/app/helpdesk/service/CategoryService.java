package com.app.helpdesk.service;

import com.app.helpdesk.dto.CategoryAndUrgencyDto;
import com.app.helpdesk.model.Category;

public interface CategoryService {
    CategoryAndUrgencyDto getAllCategoryWithUrgency();

    Category getCategoryByName(String categoryName);
}
