package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.CommentDAO;
import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.model.Comment;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.service.CommentService;
import com.app.helpdesk.service.TicketService;
import com.app.helpdesk.util.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentDAO commentDAO;
    private final TicketService ticketService;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, CommentDAO commentDAO, TicketService ticketService) {
        this.commentMapper = commentMapper;
        this.commentDAO = commentDAO;
        this.ticketService = ticketService;
    }

    @Override
    public void saveComment(User user, Long ticketId, CommentDto commentDto) {
        Ticket ticket = ticketService.getById(ticketId);
        Comment comment = commentMapper.mapToEntity(user, ticket, commentDto);
        commentDAO.saveComment(comment);
    }
}
