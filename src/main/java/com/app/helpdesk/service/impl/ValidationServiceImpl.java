package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.AttachmentDAO;
import com.app.helpdesk.dao.TicketDAO;
import com.app.helpdesk.exception.*;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import com.app.helpdesk.model.enums.Role;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.app.helpdesk.model.enums.Role.*;
import static com.app.helpdesk.model.enums.State.*;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final String DOWNLOADABLE_FILE_FORMAT = "jpg|pdf|doc|docx|png|jpeg";
    private static final String DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE = "The selected file type is not allowed. Please select a file of " +
            "one of the following types: pdf, png, doc, docx, jpg, jpeg.";
    private static final Double ALLOWED_MAXIMUM_SIZE = 5.0;
    private static final String ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE = "The size of the attached file should not be greater than 5 Mb. " +
            "Please select another file.";

    private static final Map<Role, Set<State>> ACCESS_TO_CHANGE_STATE = Map.of(
            ROLE_EMPLOYEE, Set.of(NEW, CANCELLED),
            ROLE_MANAGER, Set.of(NEW, CANCELLED, APPROVED, DECLINED),
            ROLE_ENGINEER, Set.of(IN_PROGRESS, DONE)
    );

    private final TicketDAO ticketDAO;
    private final AttachmentDAO attachmentDAO;

    @Autowired
    public ValidationServiceImpl(TicketDAO ticketDAO, AttachmentDAO attachmentDAO) {
        this.ticketDAO = ticketDAO;
        this.attachmentDAO = attachmentDAO;
    }

    @Override
    public List<String> generateErrorMessage(BindingResult bindingResult) {
        return Optional.of(bindingResult)
                .filter(BindingResult::hasErrors)
                .map(this::getErrors)
                .orElseGet(ArrayList::new);
    }

    private List<String> getErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> validateUploadFile(MultipartFile file) {

        List<String> fileUploadErrors = new ArrayList<>();

        if (file != null) {
            if (!getFileExtension(file).matches(DOWNLOADABLE_FILE_FORMAT)) {
                fileUploadErrors.add(DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE);
            }

            if (getFileSizeMegaBytes(file) > ALLOWED_MAXIMUM_SIZE) {
                fileUploadErrors.add(ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE);
            }
        }
        return fileUploadErrors;
    }

    @Override
    public void checkAccessToDraftTicket(Long userId, Long ticketId) {
        ticketDAO.checkAccessToDraftTicket(userId, ticketId)
                .orElseThrow(() -> new NoAccessToTicketException("You can't have access to current ticket"));
    }

    @Override
    public void checkAccessToDeleteAttachment(Long userId, Long attachmentId) {
        attachmentDAO.checkAccessToAttachment(userId, attachmentId)
                .orElseThrow(() -> new NoAccessToAttachmentDelete("You can't delete this attachment"));
    }

    @Override
    public void checkAccessToChangeTicketState(User user, Long ticketId, String newState) {

        Ticket ticket = getTicketForValidation(ticketId);

        if (ticket.getOwner().getEmail().equals(user.getEmail()) && ticket.getState() != DRAFT) {
            throw new NoAccessToTicketException("You can't formatted own ticket");
        }

        State state = getStateFromString(newState);

        boolean thereIsAccess = ACCESS_TO_CHANGE_STATE.get(user.getRole()).stream()
                .anyMatch(states -> states == state);

        if (!thereIsAccess) {
            throw new NoAccessToChangeTicketState("You can't change state current ticket");
        }
    }

    @Override
    public Ticket checkAccessToFeedbackTicket(User user, Long ticketId) {
        return ticketDAO.checkAccessToFeedbackTicket(user.getId(), ticketId)
                .orElseThrow(() -> new NoAccessToTicketException("You don't have access to current ticket"));
    }

    private State getStateFromString(String newState) {
        try {
            if (newState.contains("-")) {
                newState = newState.replace("-", "_");
            }
            return State.valueOf(newState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StateNotFoundException(String.format("This state \"%s\" does not exist", newState));
        }
    }

    private Ticket getTicketForValidation(Long ticketId) {
        return ticketDAO.getTicketById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format("Ticket with id %s not found", ticketId)));
    }

    private String getFileExtension(MultipartFile file) {
        if (file == null) {
            return "";
        }
        String fileName = file.getOriginalFilename();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private double getFileSizeMegaBytes(MultipartFile file) {
        return (double) file.getSize() / (1024 * 1024);
    }
}
