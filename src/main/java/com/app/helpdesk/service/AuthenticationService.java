package com.app.helpdesk.service;

import com.app.helpdesk.dto.AuthenticationRequestDto;

import java.util.Map;

public interface AuthenticationService {
    Map<Object, Object> authenticateUser(AuthenticationRequestDto requestDto);
}
