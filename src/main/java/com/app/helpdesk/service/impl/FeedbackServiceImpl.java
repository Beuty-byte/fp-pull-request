package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.FeedbackDAO;
import com.app.helpdesk.dto.FeedbackDto;
import com.app.helpdesk.model.Feedback;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.service.FeedbackService;
import com.app.helpdesk.util.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackDAO feedbackDAO;
    private final FeedbackMapper feedbackMapper;

    @Autowired
    public FeedbackServiceImpl(FeedbackDAO feedbackDAO, FeedbackMapper feedbackMapper) {
        this.feedbackDAO = feedbackDAO;
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public void save(FeedbackDto feedbackDto, User user, Ticket ticket) {
        Feedback feedback = feedbackMapper.mapToEntity(feedbackDto, ticket, user);
        feedbackDAO.saveFeedback(feedback);
    }

    @Override
    public List<FeedbackDto> getFeedbacks(User user, Long ticketId) {
        List<Feedback> feedbacks = feedbackDAO.getFeedbacks(user.getId(), ticketId);
        return feedbackMapper.mapToDto(feedbacks);
    }
}
