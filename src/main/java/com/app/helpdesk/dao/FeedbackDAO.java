package com.app.helpdesk.dao;

import com.app.helpdesk.model.Feedback;

import java.util.List;

public interface FeedbackDAO {
    void saveFeedback(Feedback feedback);
    List<Feedback> getFeedbacks(Long userId, Long ticketId);
}
