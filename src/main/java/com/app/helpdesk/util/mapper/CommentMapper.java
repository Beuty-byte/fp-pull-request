package com.app.helpdesk.util.mapper;

import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.model.Comment;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;

public interface CommentMapper {
    Comment mapToEntity(User user, Ticket ticket, CommentDto commentDto);
}
