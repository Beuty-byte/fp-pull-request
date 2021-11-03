package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.UserDAO;
import com.app.helpdesk.dto.AuthenticationRequestDto;
import com.app.helpdesk.model.User;
import com.app.helpdesk.security.jwt.JwtTokenProvider;
import com.app.helpdesk.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDAO userDAO;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserDAO userDAO) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDAO = userDAO;
    }

    @Override
    public Map<Object, Object> authenticateUser(AuthenticationRequestDto requestDto) {
        String username = requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

        User user = userDAO.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        String token = jwtTokenProvider.createToken(username, List.of(user.getRole()));

        Map<Object, Object> response = new HashMap<>();
        response.put("userRole", user.getRole());
        response.put("token", token);

        return response;
    }
}
