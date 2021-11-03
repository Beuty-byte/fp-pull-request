package com.app.helpdesk.service;

import com.app.helpdesk.dto.FeedbackDto;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;

import java.util.List;

public interface FeedbackService {
    void save(FeedbackDto feedbackDto, User user, Ticket ticket);

    List<FeedbackDto> getFeedbacks(User user, Long ticketId);
}
