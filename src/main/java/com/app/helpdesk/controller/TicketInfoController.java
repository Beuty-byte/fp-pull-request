package com.app.helpdesk.controller;

import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.exception.TicketNotFoundException;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.service.EmailService;
import com.app.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketInfoController {

    private final TicketService ticketService;

    @Autowired
    public TicketInfoController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/ticket-info/{id}")
    public ResponseEntity<TicketDto> showCurrentTicket(@PathVariable Long id) {
        TicketDto ticketById = ticketService.getTicketDtoById(id);
        return new ResponseEntity<>(ticketById, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(TicketNotFoundException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
}
