package com.app.helpdesk.controller;

import com.app.helpdesk.dto.FilterTicketDto;
import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.dto.TicketDtoWrapper;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.security.CustomUserDetails;
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
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketsController {

    private final TicketService ticketService;
    private final ValidationService validationService;

    @Autowired
    public TicketsController(TicketService ticketService, ValidationService validationService) {
        this.ticketService = ticketService;
        this.validationService = validationService;
    }

    @GetMapping("/all")
    public ResponseEntity<TicketDtoWrapper> showAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @RequestParam(name = "sort", required = false, defaultValue = "null") String sort,
                                                    @RequestParam(name = "amountTickets", required = false, defaultValue = "5") Integer amountTicketsAtPage,
                                                    @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
        User user = userDetails.getUser();
        TicketDtoWrapper ticketDtoWrapper = ticketService.getAllTicketsForCurrentUser(user, sort, amountTicketsAtPage, page);
        return new ResponseEntity<>(ticketDtoWrapper, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<TicketDtoWrapper> showMy(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestParam(name = "sort", required = false, defaultValue = "null") String sort,
                                                   @RequestParam(name = "amountTickets", required = false, defaultValue = "5") Integer amountTicketsAtPage,
                                                   @RequestParam(name = "page", required = false, defaultValue = "0") Integer page) {
        User user = userDetails.getUser();
        TicketDtoWrapper ticketDtoWrapper = ticketService.getOwnTicketsForCurrentUser(user, sort, amountTicketsAtPage, page);
        return new ResponseEntity<>(ticketDtoWrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> showCurrent(@PathVariable Long id) {
        TicketDto ticketById = ticketService.getTicketDtoById(id);
        return new ResponseEntity<>(ticketById, HttpStatus.OK);
    }

    @GetMapping("/{id}/draft")
    public ResponseEntity<TicketDto> getForDraft(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long id) {
        validationService.checkAccessToDraftTicket(userDetails.getUser().getId(), id);
        TicketDto draftTicket = ticketService.getDraft(id);
        return new ResponseEntity<>(draftTicket, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> create(@Valid @RequestPart("jsonBodyData") TicketDto ticketDto,
                                               BindingResult bindingResult,
                                               @RequestPart(name = "uploadFile", required = false) MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestParam(value = "save", required = false) String draft) {
        List<String> fileUploadErrors = validationService.validateUploadFile(file);
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(fileUploadErrors, errorMessage)) {
            errorMessage.addAll(fileUploadErrors);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        ticketService.save(ticketDto, file, userDetails.getUser(), draft);
        return ResponseEntity.created(URI.create("/tickets")).build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> update(@Valid @RequestPart("jsonBodyData") TicketDto ticketDto,
                                               BindingResult bindingResult,
                                               @RequestPart(name = "uploadFile", required = false) MultipartFile file,
                                               @AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestParam(value = "save", required = false) String draft) {
        List<String> fileUploadErrors = validationService.validateUploadFile(file);
        List<String> errorMessages = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(fileUploadErrors, errorMessages)) {
            errorMessages.addAll(fileUploadErrors);
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        ticketService.save(ticketDto, file, userDetails.getUser(), draft);
        return ResponseEntity.created(URI.create("/tickets")).build();
    }

    private boolean checkErrors(List<String> fileUploadErrors, List<String> errorMessage) {
        return !fileUploadErrors.isEmpty() || !errorMessage.isEmpty();
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterMyTickets(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody FilterTicketDto data,
                                             BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);
        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        User user = userDetails.getUser();
        List<TicketDto> ticketsDto = ticketService.filterTicket(user, data.getRequest());
        return new ResponseEntity<>(ticketsDto, HttpStatus.OK);
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }

    @PutMapping("/{ticketId}/change-state")
    public ResponseEntity<?> changeTicketsState(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable Long ticketId,
                                                @RequestParam(value = "new-state", required = false) String newState) {
        validationService.checkAccessToChangeTicketState(userDetails.getUser(), ticketId, newState);
        ticketService.changeState(userDetails.getUser(), ticketId, State.valueOf(newState.toUpperCase()));
        return ResponseEntity.created(URI.create(String.format("/tickets/change-state/%s", ticketId))).build();
    }
}
