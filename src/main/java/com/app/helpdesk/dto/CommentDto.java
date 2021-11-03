package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {

    @JsonFormat(shape = STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime date;

    @Pattern(regexp = "([\\p{Graph}|\\s*]+)", message = "wrong data")
    @Size(max = 500, message = "size comment should not be more than 500 characters")
    private String text;

    private String username;
}
