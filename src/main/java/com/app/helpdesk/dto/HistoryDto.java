package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryDto {
    @JsonFormat(shape = STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime date;
    private String action;
    private String description;
    private String username;
}
