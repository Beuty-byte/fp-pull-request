package com.app.helpdesk.controller;

import com.app.helpdesk.dto.CategoryDto;
import com.app.helpdesk.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryDto> getAllWithUrgency() {
        CategoryDto allCategory = categoryService.getAllCategoryWithUrgency();
        return new ResponseEntity<>(allCategory, HttpStatus.OK);
    }
}
