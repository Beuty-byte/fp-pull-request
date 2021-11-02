package com.app.helpdesk.dao;

import com.app.helpdesk.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findUserByEmail(String email);

    List<User> getAllManagers();

    User getCreatorTicket(Long ticketId);

    List<User> getAllEngineer();

    User getApproverTicket(Long ticketId);

    User getEngineerForFeedback(Long ticketId);
}
