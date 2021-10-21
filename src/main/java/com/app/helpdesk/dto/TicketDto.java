package com.app.helpdesk.dto;

import com.app.helpdesk.model.enums.State;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Builder(access = AccessLevel.PUBLIC)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDto {
    private Long id;

    @Pattern(regexp = "([\\p{Digit}|\\p{Lower}|\\p{Punct}|\\s*]+)", message = "wrong data")
    @Size(min = 2, max = 100, message = "size name not should be more than 100 characters and less than 2")
    @NotBlank(message = "name not should be empty")
    private String name;

    @Future(message = "you cannot select a past date")
    private LocalDate resolutionDate;

    @Pattern(regexp = "critical|high|average|low", message = "are you hacker bro?)")
    @NotBlank(message = "urgency not should be empty")
    private String urgency;

    private State status;

    private LocalDate createdOn;

    @NotBlank(message = "category not should be empty")
    private String category;

    private String owner;

    private String approver;

    private String assignee;

    @Pattern(regexp = "([\\p{Graph}|\\s*]+)", message = "wrong data")
    @Size(max = 500, message = "size comment not should be more than 500 characters")
    private String comment;

    private AttachmentDto attachment;

    @Pattern(regexp = "([\\p{Graph}|\\s*]+)", message = "wrong data")
    @Size(max = 500, message = "size description not should be more than 500 characters")
    private String description;

    private CategoryAndUrgencyDto categoryAndUrgencyDto;

    private AttachmentDto attachmentDto;

    private List<HistoryDto> histories;

    private List<CommentDto> comments;

}
