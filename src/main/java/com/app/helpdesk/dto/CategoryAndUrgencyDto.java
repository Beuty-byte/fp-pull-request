package com.app.helpdesk.dto;

import com.app.helpdesk.model.Category;
import com.app.helpdesk.model.enums.Urgency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashSet;

@AllArgsConstructor
@Getter
public class CategoryAndUrgencyDto {
    private LinkedHashSet<Category> categories;
    private Urgency[] urgencies;
}
