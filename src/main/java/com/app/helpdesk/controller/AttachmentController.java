package com.app.helpdesk.controller;

import com.app.helpdesk.exception.NotAccessToAttachmentDelete;
import com.app.helpdesk.exception.NotFoundAttachmentException;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.model.Attachment;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.AttachmentService;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final ValidationService validationService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService, ValidationService validationService) {
        this.attachmentService = attachmentService;
        this.validationService = validationService;
    }

    @GetMapping(value = "attachments/{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long id) {

        Attachment fileEntity = attachmentService.getAttachmentById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getName() + "\"")
                .contentType(MediaType.valueOf(fileEntity.getContentType()))
                .body(fileEntity.getBlob());
    }

    @DeleteMapping(value = "attachments/{id}")
    public ResponseEntity<HttpStatus> deleteAttachment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @PathVariable Long id) {
        validationService.checkAccessToDeleteAttachment(userDetails.getUser().getId(), id);
        attachmentService.deleteAttachmentById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NotFoundAttachmentException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NotAccessToAttachmentDelete e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.FORBIDDEN);
    }
}
