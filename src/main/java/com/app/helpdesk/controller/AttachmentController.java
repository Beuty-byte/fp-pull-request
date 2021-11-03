package com.app.helpdesk.controller;

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
@RequestMapping("/attachments")
public class AttachmentController {

    private static final String RESPONSE_ATTACHMENT_HEADER = "attachment; filename=\"%s\"";

    private final AttachmentService attachmentService;
    private final ValidationService validationService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService, ValidationService validationService) {
        this.attachmentService = attachmentService;
        this.validationService = validationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getById(@PathVariable Long id) {

        Attachment fileEntity = attachmentService.getAttachmentById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(RESPONSE_ATTACHMENT_HEADER, fileEntity.getName()))
                .contentType(MediaType.valueOf(fileEntity.getContentType()))
                .body(fileEntity.getBlob());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long id) {
        validationService.checkAccessToDeleteAttachment(userDetails.getUser().getId(), id);
        attachmentService.deleteAttachmentById(id);
        return ResponseEntity.noContent().build();
    }
}
