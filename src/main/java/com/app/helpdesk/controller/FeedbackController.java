package com.app.helpdesk.controller;

import com.app.helpdesk.dto.FeedbackDto;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.FeedbackService;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final ValidationService validationService;
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(ValidationService validationService, FeedbackService feedbackService) {
        this.validationService = validationService;
        this.feedbackService = feedbackService;
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<?> createFeedback(@Valid @RequestBody FeedbackDto feedbackDto,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long ticketId) {
        List<String> errors = validationService.generateErrorMessage(bindingResult);
        if (checkErrors(errors)) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        Ticket ticket = validationService.checkAccessToFeedbackTicket(userDetails.getUser(), ticketId);
        feedbackService.save(feedbackDto, userDetails.getUser(), ticket);
        return ResponseEntity.created(URI.create(String.format("feedbacks/%s", ticketId))).build();
    }

    private boolean checkErrors(List<String> errors) {
        return !errors.isEmpty();
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<FeedbackDto>> getFeedbacks(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @PathVariable Long ticketId) {
        List<FeedbackDto> feedbacks = feedbackService.getFeedbacks(userDetails.getUser(), ticketId);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }
}
