package com.app.helpdesk.dao;

import com.app.helpdesk.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    List<Ticket> getTicketsForEmployee(Long employeeId, int amountTicketsAtPage, int page);

    long getAmountOwnTicketsFromEmployee(Long employeeId);

    List<Ticket> getOwnTicketsForManager(Long managerId, int amountTicketsAtPage, int page);

    long getAmountOwnTicketsFromManager(Long managerId);

    List<Ticket> getAllTicketsForManager(Long managerId, int amountTicketsAtPage, int page);

    long getAmountAllTicketsFromManager(Long managerId);

    List<Ticket> getTicketsForEngineer(Long engineerId, int amountTicketsAtPage, int page);

    long getAmountAllTicketsFromEngineer(Long managerId);

    List<Ticket> filterTicketsForManager(String filterRequest, Long managerId);

    List<Ticket> filterTicketsForEmployee(String filterRequest, Long employeeId);

    List<Ticket> filterTicketsForEngineer(String filterRequest, Long engineerId);

    void saveTicket(Ticket ticket);

    Optional<Ticket> getTicketById(Long ticketId);

    Optional<Ticket> checkAccessToDraftTicket(Long userId, Long ticketId);

    Optional<Ticket> checkAccessToFeedbackTicket(Long userId, Long ticketId);

    void changeState(Ticket ticket);
}
