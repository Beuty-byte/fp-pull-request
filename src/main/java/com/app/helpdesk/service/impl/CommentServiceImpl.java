package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.CommentDAO;
import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.model.Comment;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.service.CommentService;
import com.app.helpdesk.util.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentDAO commentDAO;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, CommentDAO commentDAO) {
        this.commentMapper = commentMapper;
        this.commentDAO = commentDAO;
    }

    @Override
    public void saveComment(User user, Ticket ticket, CommentDto commentDto) {
        Comment comment = commentMapper.mapToEntity(user, ticket, commentDto);
        commentDAO.saveComment(comment);
    }
}
