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

    private static final String FILTER_BY = "(name like :filterRequest or desiredResolutionDate like :filterRequest or urgency like :filterRequest)";

    private final EntityManager entityManager;

    @Autowired
    public TicketDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Ticket> getTicketsForEmployee(Long employeeId, int amountTicketsAtPage, int page) {
        return entityManager.createQuery("from Ticket where owner.id = :id", Ticket.class)
                .setParameter("id", employeeId)
                .setFirstResult(amountTicketsAtPage * page)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }


    public long getAmountOwnTicketsFromEmployee(Long employeeId) {
        return (long) entityManager.createQuery("select count(*) from Ticket where owner.id = :id")
                .setParameter("id", employeeId)
                .getSingleResult();
    }

    @Override
    public List<Ticket> getOwnTicketsForManager(Long managerId, int amountTicketsAtPage, int page) {
        return entityManager.createQuery("from Ticket where owner.id = :id", Ticket.class)
                .setParameter("id", managerId)
                .setFirstResult(amountTicketsAtPage * page)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }

    @Override
    public long getAmountOwnTicketsFromManager(Long managerId) {
        return (long) entityManager.createQuery("select count(*) from Ticket where owner.id = :id")
                .setParameter("id", managerId)
                .getSingleResult();
    }

    @Override
    public List<Ticket> getAllTicketsForManager(Long managerId, int amountTicketsAtPage, int page) {
        return entityManager
                .createQuery("from Ticket where (state = 'NEW' and owner.role = 'ROLE_EMPLOYEE') or (approver.id = :id and state IN('APPROVED', 'DECLINED', 'CANCELED', 'IN_PROGRESS', 'DONE'))", Ticket.class)
                .setParameter("id", managerId)
                .setFirstResult(amountTicketsAtPage * page)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }

    @Override
    public long getAmountAllTicketsFromManager(Long managerId) {
        return (long) entityManager.createQuery("select count(*) from Ticket where (state = 'NEW' and owner.role = 'ROLE_EMPLOYEE') or (approver.id = :id and state IN('APPROVED', 'DECLINED', 'CANCELED', 'IN_PROGRESS', 'DONE'))")
                .setParameter("id", managerId)
                .getSingleResult();
    }

    @Override
    public List<Ticket> getTicketsForEngineer(Long engineerId, int amountTicketsAtPage, int page) {
        return entityManager
                .createQuery("from Ticket where (state = 'APPROVED' and owner.role IN('ROLE_EMPLOYEE', 'ROLE_MANAGER')) or (assignee.id = :id and state IN('IN_PROGRESS', 'DONE'))", Ticket.class)
                .setParameter("id", engineerId)
                .setFirstResult(amountTicketsAtPage * page)
                .setMaxResults(amountTicketsAtPage)
                .getResultList();
    }

    @Override
    public long getAmountAllTicketsFromEngineer(Long managerId) {
        return (long) entityManager.createQuery("select count(*) from Ticket where (state = 'APPROVED' and owned.role IN('ROLE_EMPLOYEE', 'ROLE_MANAGER')) or (assignee.id = :id and state IN('IN_PROGRESS', 'DONE'))")
                .setParameter("id", managerId)
                .getSingleResult();
    }

    @Override
    public List<Ticket> filterTicketsForManager(String filterRequest, Long managerId) {

        String hql = "from Ticket where (owner.id = :id or approver.id = :id) and " + FILTER_BY;

        return entityManager.createQuery(hql, Ticket.class)
                .setParameter("id", managerId)
                .setParameter("filterRequest", "%" + filterRequest + "%").getResultList();
    }

    @Override
    public List<Ticket> filterTicketsForEmployee(String filterRequest, Long employeeId) {

        String hql = "from Ticket where (owner.id = :id) and " + FILTER_BY;

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
        return entityManager.createQuery("from Ticket where owner.id = :userId and id = :id and state = 'DRAFT'", Ticket.class)
                .setParameter("userId", userId)
                .setParameter("id", ticketId)
                .getResultStream()
                .findAny();
    }

    @Override
    public Optional<Ticket> checkAccessToFeedbackTicket(Long userId, Long ticketId) {
        return entityManager.createQuery("from Ticket where owner.id = :userId and id = :id and state = 'DONE'", Ticket.class)
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
