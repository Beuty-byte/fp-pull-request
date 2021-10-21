package com.app.helpdesk.controller;

import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.exception.NotAccessToTicketException;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.CategoryService;
import com.app.helpdesk.service.TicketService;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketsDraftController {

    private final TicketService ticketService;
    private final ValidationService validationService;

    @Autowired
    public TicketsDraftController(CategoryService categoryService, TicketService ticketService, ValidationService validationService) {
        this.ticketService = ticketService;
        this.validationService = validationService;
    }

    @GetMapping("draft-ticket/{id}")
    public ResponseEntity<TicketDto> getTicketForDraft(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @PathVariable Long id) {

        validationService.checkAccessToDraftTicket(userDetails.getUser().getId(), id);

        TicketDto draftTicket = ticketService.getDraftTicket(id);
        return new ResponseEntity<>(draftTicket, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NotAccessToTicketException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.FORBIDDEN);
    }
}
