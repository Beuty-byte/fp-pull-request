package com.app.helpdesk.service;

import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;

public interface CommentService {
    void saveComment(User user, Ticket ticket, CommentDto commentDto);
}
