package com.app.helpdesk.controller;

import com.app.helpdesk.dto.AuthenticationRequestDto;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.service.AuthenticationService;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ValidationService validationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, ValidationService validationService) {
        this.authenticationService = authenticationService;
        this.validationService = validationService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequestDto requestDto,
                                   BindingResult bindingResult) {

        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (!errorMessage.isEmpty()) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        Map<Object, Object> response = authenticationService.authenticateUser(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(UsernameNotFoundException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
}
