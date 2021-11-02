package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterTicketDto {
    @Pattern(regexp = "([\\p{Digit}|\\p{Lower}|\\p{Punct}]+)", message = "wrong filter request")
    private String request;
}
