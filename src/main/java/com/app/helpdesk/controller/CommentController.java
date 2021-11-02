package com.app.helpdesk.controller;

import com.app.helpdesk.dto.CommentDto;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.CommentService;
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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final ValidationService validationService;

    @Autowired
    public CommentController(CommentService commentService, ValidationService validationService) {
        this.commentService = commentService;
        this.validationService = validationService;
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<?> createNewComment(@Valid @RequestBody CommentDto commentDto,
                                              BindingResult bindingResult,
                                              @PathVariable Long ticketId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<String> errorList = validationService.generateErrorMessage(bindingResult);
        if (checkErrors(errorList)) {
            return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
        }
        commentService.saveComment(userDetails.getUser(), ticketId, commentDto);
        return ResponseEntity.created(URI.create(String.format("/comments/%s", ticketId))).build();
    }

    private boolean checkErrors(List<String> errorList) {
        return !errorList.isEmpty();
    }
}
