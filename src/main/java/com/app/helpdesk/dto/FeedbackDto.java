package com.app.helpdesk.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Builder(access = AccessLevel.PUBLIC)
public class FeedbackDto {
    private String feedbackComment;

    @Pattern(regexp = "bad|so-so|normal|good|exelent", message = "Wrong ticket gradle")
    private String feedbackGradle;

    private LocalDate localDate;
}
