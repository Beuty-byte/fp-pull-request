package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackDto {
    private String comment;

    @Pattern(regexp = "bad|so-so|normal|good|exelent", message = "Wrong ticket grade")
    @NotBlank(message = "grade should not be empty")
    private String grade;

    private LocalDate localDate;
}
