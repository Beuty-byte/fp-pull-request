package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder(access = AccessLevel.PUBLIC)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private LocalDateTime date;

    @Pattern(regexp = "([\\p{Graph}|\\s*]+)", message = "wrong data")
    @Size(max = 500, message = "size comment not should be more than 500 characters")
    private String text;

    private String username;
}
