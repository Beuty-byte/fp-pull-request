package com.app.helpdesk.util.mapper;

import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.model.History;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.enums.State;

public interface HistoryMapper {
    History getHistoryAfterTicketCreate(Ticket ticket);

    History getHistoryAfterTicketUpdate(Ticket ticket);

    History getHistoryAfterTicketChangeState(State previousState, Ticket ticket);

    History getHistoryAfterAttachmentCreate(Attachment attachment);

    History getHistoryAfterAttachmentRemove(Attachment attachment);
}
