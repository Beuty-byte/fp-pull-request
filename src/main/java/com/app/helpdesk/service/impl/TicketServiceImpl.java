package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.TicketDAO;
import com.app.helpdesk.dto.CategoryDto;
import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.dto.TicketDtoWrapper;
import com.app.helpdesk.exception.TicketNotFoundException;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.service.CategoryService;
import com.app.helpdesk.service.TicketService;
import com.app.helpdesk.util.mapper.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.app.helpdesk.model.enums.Role.*;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private static final Map<String, Comparator<Ticket>> TICKET_SORTERS = Map.of(
            "id-asc", Comparator.comparing(Ticket::getId),
            "id-desc", Comparator.comparing(Ticket::getId, Comparator.reverseOrder()),
            "name-asc", Comparator.comparing(Ticket::getName),
            "name-desc", Comparator.comparing(Ticket::getName, Comparator.reverseOrder()),
            "desireddate-asc", Comparator.comparing(Ticket::getDesiredResolutionDate),
            "desireddate-desc", Comparator.comparing(Ticket::getDesiredResolutionDate, Comparator.reverseOrder()),
            "urgency-asc", Comparator.comparing(Ticket::getUrgency),
            "urgency-desc", Comparator.comparing(Ticket::getUrgency, Comparator.reverseOrder()),
            "status-asc", Comparator.comparing(Ticket::getState),
            "status-desc", Comparator.comparing(Ticket::getState, Comparator.reverseOrder())
    );
    private static final String TICKET_ERROR_MESSAGE = "Ticket with id %s not found";

    private final TicketDAO ticketDAO;
    private final TicketMapper ticketMapper;
    private final CategoryService categoryService;

    @Autowired
    public TicketServiceImpl(TicketDAO ticketDAO, TicketMapper ticketMapper, CategoryService categoryService) {
        this.ticketDAO = ticketDAO;
        this.ticketMapper = ticketMapper;
        this.categoryService = categoryService;
    }

    @Override
    public TicketDtoWrapper getAllTicketsForCurrentUser(User user, String sortParam, int amountTicketsAtPage, int page) {

        List<Ticket> allTickets = new ArrayList<>();
        long amountTickets = 0;

        if (user.getRole().equals(ROLE_EMPLOYEE)) {
            allTickets = ticketDAO.getTicketsForEmployee(user.getId(), amountTicketsAtPage, page);
            amountTickets = ticketDAO.getAmountOwnTicketsFromEmployee(user.getId());
        }

        if (user.getRole().equals(ROLE_MANAGER)) {
            allTickets = ticketDAO.getAllTicketsForManager(user.getId(), amountTicketsAtPage, page);
            amountTickets = ticketDAO.getAmountAllTicketsFromManager(user.getId());
        }

        if (user.getRole().equals(ROLE_ENGINEER)) {
            allTickets = ticketDAO.getTicketsForEngineer(user.getId(), amountTicketsAtPage, page);
            amountTickets = ticketDAO.getAmountAllTicketsFromEngineer(user.getId());
        }

        sortTicketsByParam(allTickets, sortParam);

        List<TicketDto> ticketDtos = ticketMapper.mapToDto(allTickets);
        TicketDtoWrapper ticketDtoWrapper = new TicketDtoWrapper();
        ticketDtoWrapper.setTicketDtoList(ticketDtos);
        ticketDtoWrapper.setAmountTickets(amountTickets);
        return ticketDtoWrapper;
    }

    @Override
    public TicketDtoWrapper getOwnTicketsForCurrentUser(User user, String sortParam, int amountTicketsAtPage, int page) {

        List<Ticket> ownTickets = new ArrayList<>();
        long amountTickets = 0;

        if (user.getRole().equals(ROLE_EMPLOYEE)) {
            ownTickets = ticketDAO.getTicketsForEmployee(user.getId(), amountTicketsAtPage, page);
            amountTickets = ticketDAO.getAmountOwnTicketsFromEmployee(user.getId());
        }

        if (user.getRole().equals(ROLE_MANAGER)) {
            ownTickets = ticketDAO.getOwnTicketsForManager(user.getId(), amountTicketsAtPage, page);
            amountTickets = ticketDAO.getAmountOwnTicketsFromManager(user.getId());
        }

        if (user.getRole().equals(ROLE_ENGINEER)) {
            ownTickets = ticketDAO.getTicketsForEngineer(user.getId(), amountTicketsAtPage, page);
            amountTickets = ticketDAO.getAmountAllTicketsFromEngineer(user.getId());
        }

        sortTicketsByParam(ownTickets, sortParam);

        List<TicketDto> ticketDtos = ticketMapper.mapToDto(ownTickets);
        TicketDtoWrapper ticketDtoWrapper = new TicketDtoWrapper();
        ticketDtoWrapper.setTicketDtoList(ticketDtos);
        ticketDtoWrapper.setAmountTickets(amountTickets);
        return ticketDtoWrapper;
    }

    @Override
    public List<TicketDto> filterTicket(User user, String filterRequest) {

        List<Ticket> filteredTickets = new ArrayList<>();

        if (user.getRole().equals(ROLE_EMPLOYEE)) {
            filteredTickets = ticketDAO.filterTicketsForEmployee(filterRequest, user.getId());
        }

        if (user.getRole().equals(ROLE_MANAGER)) {
            filteredTickets = ticketDAO.filterTicketsForManager(filterRequest, user.getId());
        }

        if (user.getRole().equals(ROLE_ENGINEER)) {
            filteredTickets = ticketDAO.filterTicketsForEngineer(filterRequest, user.getId());
        }

        return ticketMapper.mapToDto(filteredTickets);
    }

    @Override
    public void save(TicketDto ticketDto, MultipartFile file, User user, String draft) {
        Ticket ticket = ticketMapper.mapToEntity(ticketDto, file, user, draft);
        ticketDAO.saveTicket(ticket);
    }

    @Override
    public TicketDto getTicketDtoById(Long ticketId) {
        Ticket ticket = getById(ticketId);
        return ticketMapper.mapToDto(ticket);
    }

    @Override
    public Ticket getById(Long ticketId) {
        return ticketDAO.getTicketById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format(TICKET_ERROR_MESSAGE, ticketId)));
    }

    @Override
    public TicketDto getDraft(Long ticketId) {
        CategoryDto allCategory = categoryService.getAllCategoryWithUrgency();
        Ticket ticket = ticketDAO.getTicketById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format(TICKET_ERROR_MESSAGE, ticketId)));
        return ticketMapper.mapToDraftDto(ticket, allCategory);
    }

    @Override
    public void changeState(User user, Long ticketId, State newState) {
        Ticket ticket = ticketDAO.getTicketById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format(TICKET_ERROR_MESSAGE, ticketId)));

        if (newState == State.APPROVED) {
            ticket.setState(newState);
            ticket.setApprover(user);
        } else if (newState == State.IN_PROGRESS) {
            ticket.setState(newState);
            ticket.setAssignee(user);
        } else {
            ticket.setState(newState);
        }
        ticketDAO.changeState(ticket);
    }

    private void sortTicketsByParam(List<Ticket> tickets, String sortParam) {
        tickets.sort(TICKET_SORTERS.getOrDefault(sortParam, Comparator.comparing(Ticket::getUrgency)
                .thenComparing(Ticket::getDesiredResolutionDate, Comparator.reverseOrder())));
    }
}
