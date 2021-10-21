package com.app.helpdesk.util.listener;

import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
public class TicketListener {

    private static HistoryService historyService;

    @Autowired
    public void setHistoryService(final HistoryService historyService) {
        TicketListener.historyService = historyService;
    }

    @PostPersist
    void afterPersist(Ticket ticket) {
        historyService.saveHistoryAfterCreateTicket(ticket);
    }

    @PostUpdate
    void afterUpdate(Ticket ticket) {
        State previousState = ticket.getPreviousStateHolder().getState();
        if (previousState == ticket.getState()) {
            historyService.saveHistoryAfterUpdateTicket(ticket);
        } else {
            historyService.saveHistoryAfterUpdateState(previousState, ticket);
        }
    }
}
