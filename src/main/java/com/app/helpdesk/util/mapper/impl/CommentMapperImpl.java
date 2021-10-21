package com.app.helpdesk.util.mapper.impl;

import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.model.Comment;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.util.mapper.CommentMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public Comment mapToEntity(User user, Ticket ticket, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setUser(user);
        comment.setText(commentDto.getText());
        return comment;
    }
}
