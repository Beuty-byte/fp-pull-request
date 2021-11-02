package com.app.helpdesk.util.listener;

import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.service.EmailGenerateService;
import com.app.helpdesk.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import static com.app.helpdesk.model.enums.State.DRAFT;

@Component
public class TicketListener {

    private static HistoryService historyService;
    private static EmailGenerateService emailGenerateService;

    @Autowired
    public void setHistoryService(final HistoryService historyService) {
        TicketListener.historyService = historyService;
    }

    @Autowired
    public void setEmailService(final EmailGenerateService emailGenerateService) {
        TicketListener.emailGenerateService = emailGenerateService;
    }


    @PostPersist
    void afterPersist(Ticket ticket) {
        historyService.saveHistoryAfterCreateTicket(ticket);
        if (ticket.getState() != DRAFT) {
            emailGenerateService.acceptTicket(ticket);
        }
    }

    @PostUpdate
    void afterUpdate(Ticket ticket) {
        State previousState = ticket.getPreviousStateHolder().getState();
        emailGenerateService.acceptTicket(ticket);
        if (previousState == ticket.getState()) {
            historyService.saveHistoryAfterUpdateTicket(ticket);
        } else {
            historyService.saveHistoryAfterUpdateState(previousState, ticket);
        }
    }
}
