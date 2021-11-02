package com.app.helpdesk.service;

import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.dto.TicketDtoWrapper;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.State;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    TicketDtoWrapper getAllTicketsForCurrentUser(User user, String sortParam, int amountTicketsAtPage, int page);

    TicketDtoWrapper getOwnTicketsForCurrentUser(User user, String sortParam, int amountTicketsAtPage, int page);

    List<TicketDto> filterTicket(User user, String filterRequest);

    void save(TicketDto ticketDto, MultipartFile file, User user, String draft);

    TicketDto getTicketDtoById(Long ticketId);

    Ticket getById(Long ticketId);

    TicketDto getDraft(Long ticketId);

    void changeState(User user, Long ticketId, State newState);
}
