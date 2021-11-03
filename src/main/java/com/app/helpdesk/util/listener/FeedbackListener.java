package com.app.helpdesk.util.listener;

import com.app.helpdesk.model.Feedback;
import com.app.helpdesk.service.EmailGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;

@Component
public class FeedbackListener {

    private static EmailGenerateService emailGenerateService;

    @Autowired
    public void setEmailService(final EmailGenerateService emailGenerateService) {
        FeedbackListener.emailGenerateService = emailGenerateService;
    }

    @PostPersist
    void afterPersist(Feedback feedback) {
        emailGenerateService.acceptFeedback(feedback);
    }
}
