package com.app.helpdesk.util.mapper;

import com.app.helpdesk.dto.FeedbackDto;
import com.app.helpdesk.model.Feedback;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;

import java.util.List;

public interface FeedbackMapper {
    Feedback mapToEntity(FeedbackDto feedbackDto, Ticket ticket, User user);
    List<FeedbackDto> mapToDto(List<Feedback> feedbacks);
}
