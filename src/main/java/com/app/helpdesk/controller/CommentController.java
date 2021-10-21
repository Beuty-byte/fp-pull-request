package com.app.helpdesk.controller;

import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.exception.TicketNotFoundException;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;
    private final TicketService ticketService;
    private final ValidationService validationService;

    @Autowired
    public CommentController(CommentService commentService, TicketService ticketService, ValidationService validationService) {
        this.commentService = commentService;
        this.ticketService = ticketService;
        this.validationService = validationService;
    }

    @PostMapping("/comments/{ticketId}")
    public ResponseEntity<?> createNewComment(@Valid @RequestBody CommentDto commentDto,
                                              BindingResult bindingResult,
                                              @PathVariable Long ticketId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<String> errorList = validationService.generateErrorMessage(bindingResult);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
        }

        Ticket ticket = ticketService.getTicketById(ticketId);
        commentService.saveComment(userDetails.getUser(), ticket, commentDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(TicketNotFoundException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }
}
