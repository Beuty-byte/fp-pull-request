package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.HistoryDAO;
import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.model.History;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.service.HistoryService;
import com.app.helpdesk.util.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final HistoryDAO historyDAO;
    private final HistoryMapper historyMapper;

    @Autowired
    public HistoryServiceImpl(HistoryDAO historyDAO, HistoryMapper historyMapper) {
        this.historyDAO = historyDAO;
        this.historyMapper = historyMapper;
    }

    @Override
    public void saveHistoryAfterCreateTicket(Ticket ticket) {
        History history = historyMapper.getHistoryAfterTicketCreate(ticket);
        historyDAO.saveHistory(history);
    }

    @Override
    public void saveHistoryAfterUpdateTicket(Ticket ticket) {
        History history = historyMapper.getHistoryAfterTicketUpdate(ticket);
        historyDAO.saveHistory(history);
    }

    @Override
    public void saveHistoryAfterUpdateState(State previousState, Ticket ticket) {
        History history = historyMapper.getHistoryAfterTicketChangeState(previousState, ticket);
        historyDAO.saveHistory(history);
    }

    @Override
    public void saveHistoryAfterCreateAttachment(Attachment attachment) {
        History history = historyMapper.getHistoryAfterAttachmentCreate(attachment);
        historyDAO.saveHistory(history);
    }

    @Override
    public void saveHistoryAfterRemoveAttachment(Attachment attachment) {
        History history = historyMapper.getHistoryAfterAttachmentRemove(attachment);
        historyDAO.saveHistory(history);
    }
}
