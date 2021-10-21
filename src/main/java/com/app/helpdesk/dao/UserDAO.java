package com.app.helpdesk.dao;

import com.app.helpdesk.model.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> findUserByEmail(String email);
}
