package com.app.helpdesk.util.mapper.impl;

import com.app.helpdesk.dto.FeedbackDto;
import com.app.helpdesk.model.Feedback;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.util.mapper.FeedbackMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackMapperImpl implements FeedbackMapper {
    @Override
    public Feedback mapToEntity(FeedbackDto feedbackDto, Ticket ticket, User user) {
        Feedback feedback = new Feedback();
        feedback.setRate(feedbackDto.getFeedbackGradle());
        feedback.setText(feedbackDto.getFeedbackComment());
        feedback.setTicket(ticket);
        feedback.setUser(user);
        return feedback;
    }

    @Override
    public List<FeedbackDto> mapToDto(List<Feedback> feedbacks) {
        return feedbacks.stream().map(feedback -> FeedbackDto.builder()
                .feedbackComment(feedback.getText())
                .localDate(feedback.getDate())
                .feedbackGradle(feedback.getRate())
                .build())
                .collect(Collectors.toList());
    }
}
