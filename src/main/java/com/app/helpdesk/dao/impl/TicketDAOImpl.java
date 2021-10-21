package com.app.helpdesk.dao.impl;

import com.app.helpdesk.dao.TicketDAO;
import com.app.helpdesk.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDAOImpl implements TicketDAO {

    private final EntityManager entityManager;
    private static final String FILTER_BY = "(name like :filterRequest or desiredResolutionDate like :filterRequest or urgency like :filterRequest)";

    @Autowired
    public TicketDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Ticket> getTicketsForEmployee(Long employeeId, int amountTicketsAtPage) {
        return entityManager.createQuery("from Ticket where owned.id = :id", Ticket.class)
                .setParameter("id", employeeId)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }


    @Override
    public List<Ticket> getOwnTicketsForManager(Long managerId, int amountTicketsAtPage) {
        return entityManager.createQuery("from Ticket where owned.id = :id", Ticket.class)
                .setParameter("id", managerId)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }

    @Override
    public List<Ticket> getAllTicketsForManager(Long managerId, int amountTicketsAtPage) {
        return entityManager
                .createQuery("from Ticket where (state = 'NEW' and owned.role = 'ROLE_EMPLOYEE') or (approver.id = :id and state IN('APPROVED', 'DECLINED', 'CANCELED', 'IN_PROGRESS', 'DONE'))", Ticket.class)
                .setParameter("id", managerId)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }

    @Override
    public List<Ticket> getTicketsForEngineer(Long engineerId, int amountTicketsAtPage) {
        return entityManager
                .createQuery("from Ticket where (state = 'APPROVED' and owned.role IN('ROLE_EMPLOYEE', 'ROLE_MANAGER')) or (assignee.id = :id and state IN('IN_PROGRESS', 'DONE'))", Ticket.class)
                .setParameter("id", engineerId)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }

    @Override
    public List<Ticket> filterTicketsForManager(String filterRequest, Long managerId) {

        String hql = "from Ticket where (owned.id = :id or approver.id = :id) and " + FILTER_BY;

        return entityManager.createQuery(hql, Ticket.class)
                .setParameter("id", managerId)
                .setParameter("filterRequest", "%" + filterRequest + "%").getResultList();
    }

    @Override
    public List<Ticket> filterTicketsForEmployee(String filterRequest, Long employeeId) {

        String hql = "from Ticket where (owned.id = :id) and " + FILTER_BY;

        return entityManager.createQuery(hql, Ticket.class)
                .setParameter("id", employeeId)
                .setParameter("filterRequest", "%" + filterRequest + "%").getResultList();
    }

    @Override
    public List<Ticket> filterTicketsForEngineer(String filterRequest, Long engineerId) {

        String hql = "from Ticket where (assignee.id = :id) and " + FILTER_BY;

        return entityManager.createQuery(hql, Ticket.class)
                .setParameter("id", engineerId)
                .setParameter("filterRequest", "%" + filterRequest + "%").getResultList();
    }

    @Override
    public void saveTicket(Ticket ticket) {
        entityManager.merge(ticket);
    }

    @Override
    public Optional<Ticket> getTicketById(Long ticketId) {
        return entityManager.createQuery("from Ticket where id = :id", Ticket.class)
                .setParameter("id", ticketId)
                .getResultStream()
                .findAny();
    }

    @Override
    public Optional<Ticket> checkAccessToDraftTicket(Long userId, Long ticketId) {
        return entityManager.createQuery("from Ticket where owned.id = :userId and id = :id and state = 'DRAFT'", Ticket.class)
                .setParameter("userId", userId)
                .setParameter("id", ticketId)
                .getResultStream()
                .findAny();
    }

    @Override
    public Optional<Ticket> checkAccessToFeedbackTicket(Long userId, Long ticketId) {
        return entityManager.createQuery("from Ticket where owned.id = :userId and id = :id and state = 'DONE'", Ticket.class)
                .setParameter("userId", userId)
                .setParameter("id", ticketId)
                .getResultStream()
                .findAny();
    }

    @Override
    public void changeState(Ticket ticket) {
        entityManager.merge(ticket);
    }
}
