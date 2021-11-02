package com.app.helpdesk.service.impl;

import com.app.helpdesk.model.Feedback;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.service.EmailGenerateService;
import com.app.helpdesk.service.EmailService;
import com.app.helpdesk.util.email.MailSendState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import static com.app.helpdesk.model.enums.State.NEW;
import static com.app.helpdesk.util.email.MailSendState.*;

@Service
public class EmailGenerateServiceImpl implements EmailGenerateService {

    private final Map<MailSendState, Consumer<Long>> sendStatus;
    private final EmailService emailService;

    @Autowired
    public EmailGenerateServiceImpl(EmailService emailService) {
        sendStatus = Map.of(
                NEW_NEW, emailService::sendNewTicketMail,
                DRAFT_NEW, emailService::sendNewTicketMail,
                DECLINED_NEW, emailService::sendNewTicketMail,
                NEW_APPROVED, emailService::sendApprovedTicketMail,
                NEW_CANCELLED, emailService::sendNewCancelledTicketMail,
                NEW_DECLINED, emailService::sendDeclinedTicketMail,
                APPROVED_CANCELLED, emailService::sendApprovedCancelledTicketMail,
                IN_PROGRESS_DONE, emailService::sendDoneTicketMail
        );
        this.emailService = emailService;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void acceptTicket(Ticket ticket) {
        Map<State, State> ticketState = Map.of(
                ticket.getPreviousStateHolder() == null ? NEW : ticket.getPreviousStateHolder().getState(),
                ticket.getState()
        );
        Arrays.stream(MailSendState.values())
                .filter(states -> states.isPreviousStateToCurrentStateMapEqualTo(ticketState))
                .findFirst()
                .ifPresent(state -> sendStatus.get(state).accept(ticket.getId()));
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void acceptFeedback(Feedback feedback) {
        emailService.sendFeedback(feedback.getTicket().getId());
    }
}
