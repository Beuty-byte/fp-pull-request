package com.app.helpdesk.controller;

import com.app.helpdesk.dto.FilterTicketDto;
import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.exception.NotAccessToChangeTicketState;
import com.app.helpdesk.exception.NotAccessToTicketException;
import com.app.helpdesk.exception.NotFoundStateException;
import com.app.helpdesk.exception.TicketNotFoundException;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.TicketService;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TicketsController {

    private final TicketService ticketService;
    private final ValidationService validationService;

    @Autowired
    public TicketsController(TicketService ticketService, ValidationService validationService) {
        this.ticketService = ticketService;
        this.validationService = validationService;
    }

    @GetMapping("all-tickets")
    public ResponseEntity<List<TicketDto>> showAllTickets(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestParam(name = "sort", required = false, defaultValue = "null") String sort,
                                                          @RequestParam(name = "amountTickets", required = false, defaultValue = "5") Integer amountTicketsAtPage) {
        User user = userDetails.getUser();
        List<TicketDto> allTicketForCurrentUser = ticketService.getAllTicketForCurrentUser(user, sort, amountTicketsAtPage);
        return new ResponseEntity<>(allTicketForCurrentUser, HttpStatus.OK);
    }

    @GetMapping("my-tickets")
    public ResponseEntity<List<TicketDto>> showMyTickets(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestParam(name = "sort", required = false, defaultValue = "null") String sort,
                                                         @RequestParam(name = "amountTickets", required = false, defaultValue = "5") Integer amountTicketsAtPage) {
        User user = userDetails.getUser();
        List<TicketDto> ownTicketForCurrentUser = ticketService.getOwnTicketForCurrentUser(user, sort, amountTicketsAtPage);
        return new ResponseEntity<>(ownTicketForCurrentUser, HttpStatus.OK);
    }

    @PostMapping("my-tickets")
    public ResponseEntity<?> filterMyTickets(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody FilterTicketDto data,
                                             BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (!errorMessage.isEmpty()) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        User user = userDetails.getUser();
        List<TicketDto> ticketDto = ticketService.filterTicket(user, data.getFilterRequest());

        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    @PutMapping("change-state/{ticketId}")
    public ResponseEntity<?> changeTicketsState(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable Long ticketId,
                                                @RequestParam(value = "new-state", required = false) String newState) {

        validationService.checkAccessToChangeTicketState(userDetails.getUser(), ticketId, newState);

        ticketService.changeState(userDetails.getUser(), ticketId, State.valueOf(newState.toUpperCase()));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NotAccessToTicketException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(TicketNotFoundException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NotAccessToChangeTicketState e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NotFoundStateException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
}
