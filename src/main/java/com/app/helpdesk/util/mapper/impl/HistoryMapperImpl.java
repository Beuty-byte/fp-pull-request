package com.app.helpdesk.util.mapper.impl;

import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.model.History;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.util.mapper.HistoryMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapperImpl implements HistoryMapper {

    private static final String ACTION_CREATE_TICKET = "Ticket is created";
    private static final String ACTION_UPDATE_TICKET = "Ticket is edited";
    private static final String ACTION_CHANGE_TICKET_STATUS = "Ticket Status is changed";

    @Override
    public History getHistoryAfterTicketCreate(Ticket ticket) {
        History history = new History();
        history.setUser(getUser());
        history.setAction(ACTION_CREATE_TICKET);
        history.setDescription(ACTION_CREATE_TICKET);
        history.setTicket(ticket);
        return history;
    }

    @Override
    public History getHistoryAfterTicketUpdate(Ticket ticket) {
        History history = new History();
        history.setUser(getUser());
        history.setAction(ACTION_UPDATE_TICKET);
        history.setDescription(ACTION_UPDATE_TICKET);
        history.setTicket(ticket);
        return history;
    }

    @Override
    public History getHistoryAfterTicketChangeState(State previousState, Ticket ticket) {
        History history = new History();
        history.setUser(getUser());
        history.setAction(ACTION_CHANGE_TICKET_STATUS);
        history.setDescription(String.format("Ticket Status is changed from %s to %s.", previousState, ticket.getState()));
        history.setTicket(ticket);
        return history;
    }

    @Override
    public History getHistoryAfterAttachmentCreate(Attachment attachment) {
        History history = new History();
        history.setUser(getUser());
        history.setAction("File is attached");
        history.setDescription(String.format("File is attached: %s", attachment.getName()));
        history.setTicket(attachment.getTicket());
        return history;
    }

    @Override
    public History getHistoryAfterAttachmentRemove(Attachment attachment) {
        History history = new History();
        history.setUser(getUser());
        history.setAction("File is removed");
        history.setDescription(String.format("File is removed : %s", attachment.getName()));
        history.setTicket(attachment.getTicket());
        return history;
    }

    private User getUser() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetails.getUser();
    }
}
