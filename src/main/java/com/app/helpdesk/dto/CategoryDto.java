package com.app.helpdesk.dto;

import com.app.helpdesk.model.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashSet;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {
    private LinkedHashSet<Category> categories;
}
