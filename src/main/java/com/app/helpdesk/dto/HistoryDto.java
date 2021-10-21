package com.app.helpdesk.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PUBLIC)
@Getter
public class HistoryDto {
    private LocalDateTime date;
    private String action;
    private String description;
    private String username;
}
