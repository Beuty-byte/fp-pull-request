package com.app.helpdesk.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class FilterTicketDto {
    @Pattern(regexp = "([\\p{Digit}|\\p{Lower}|\\p{Punct}]+)", message = "wrong filter request")
    private String filterRequest;
}
