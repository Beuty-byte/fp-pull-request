package com.app.helpdesk.service;

import com.app.helpdesk.model.Feedback;
import com.app.helpdesk.model.Ticket;

public interface EmailGenerateService {
    void acceptTicket(Ticket ticket);

    void acceptFeedback(Feedback feedback);
}
