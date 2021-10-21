package com.app.helpdesk.controller;

import com.app.helpdesk.dto.CategoryAndUrgencyDto;
import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.exception.NoSuchCategoryException;
import com.app.helpdesk.exception_handling.ExceptionInfo;
import com.app.helpdesk.security.CustomUserDetails;
import com.app.helpdesk.service.CategoryService;
import com.app.helpdesk.service.TicketService;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CreateTicketController {

    private final CategoryService categoryService;
    private final ValidationService validationService;
    private final TicketService ticketService;

    @Autowired
    public CreateTicketController(CategoryService categoryService, ValidationService validationService, TicketService ticketService) {
        this.categoryService = categoryService;
        this.validationService = validationService;
        this.ticketService = ticketService;
    }

    @GetMapping("create-tickets")
    public ResponseEntity<CategoryAndUrgencyDto> getCategoryAndUrgency() {
        CategoryAndUrgencyDto allCategory = categoryService.getAllCategoryWithUrgency();
        return new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @PostMapping(value = "create-tickets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> createNewTicket(@Valid @RequestPart("jsonBodyData") TicketDto ticketDto,
                                             BindingResult bindingResult,
                                             @RequestPart(name = "uploadFile", required = false) MultipartFile file,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestParam(value = "save", required = false) String draft) {

        List<String> fileUploadErrors = validationService.validateUploadFile(file);
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (!fileUploadErrors.isEmpty() || !errorMessage.isEmpty()) {
            errorMessage.addAll(fileUploadErrors);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        ticketService.saveTicket(ticketDto, file, userDetails.getUser(), draft);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handleException(NoSuchCategoryException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
}
