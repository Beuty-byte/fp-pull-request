package com.app.helpdesk.service;

import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.model.User;

public interface CommentService {
    void saveComment(User user, Long ticketId, CommentDto commentDto);
}
