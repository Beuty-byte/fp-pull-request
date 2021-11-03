package com.app.helpdesk.service;

import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.enums.State;

public interface HistoryService {
    void saveHistoryAfterCreateTicket(Ticket ticket);

    void saveHistoryAfterUpdateTicket(Ticket ticket);

    void saveHistoryAfterUpdateState(State previousState, Ticket ticket);

    void saveHistoryAfterCreateAttachment(Attachment attachment);

    void saveHistoryAfterRemoveAttachment(Attachment attachment);
}
