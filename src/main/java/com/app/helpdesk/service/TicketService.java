package com.app.helpdesk.service;

import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.State;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    List<TicketDto> getAllTicketForCurrentUser(User user, String sortParam, int amountTicketsAtPage);

    List<TicketDto> getOwnTicketForCurrentUser(User user, String sortParam, int amountTicketsAtPage);

    List<TicketDto> filterTicket(User user, String filterRequest);

    void saveTicket(TicketDto ticketDto, MultipartFile file, User user, String draft);

    TicketDto getTicketDtoById(Long ticketId);

    Ticket getTicketById(Long ticketId);

    TicketDto getDraftTicket(Long ticketId);

    void changeState(User user, Long ticketId, State newState);
}
