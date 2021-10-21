package com.app.helpdesk.service;

import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ValidationService {
    List<String> generateErrorMessage(BindingResult bindingResult);

    List<String> validateUploadFile(MultipartFile file);

    void checkAccessToDraftTicket(Long userId, Long ticketId);

    void checkAccessToDeleteAttachment(Long userId, Long attachmentId);

    void checkAccessToChangeTicketState(User user, Long ticketId, String newState);

    Ticket checkAccessToFeedbackTicket(User user, Long ticketId);
}
