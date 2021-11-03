package com.app.helpdesk.service.impl;

import com.app.helpdesk.dao.UserDAO;
import com.app.helpdesk.model.User;
import com.app.helpdesk.service.EmailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Value("${spring.permitted.url}")
    private String baseUrl;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    private final JavaMailSender emailSender;
    private final UserDAO userDAO;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, UserDAO userDAO) {
        this.emailSender = emailSender;
        this.userDAO = userDAO;
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel, String template) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
        sendHtmlMessage(to, subject, htmlBody);
    }

    @SneakyThrows
    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sendFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }


    @Override
    public void sendNewTicketMail(Long ticketId) {
        List<User> recipients = userDAO.getAllManagers();
        recipients.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(), "New ticket for approval", Map.of(
                "ticketId", ticketId,
                "baseUrl", baseUrl
        ), "template-new.html"));
    }

    @Override
    public void sendApprovedTicketMail(Long ticketId) {
        List<User> recipients = userDAO.getAllEngineer();
        recipients.add(userDAO.getCreatorTicket(ticketId));
        recipients.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was approved", Map.of(
                "ticketId", ticketId,
                "baseUrl", baseUrl
        ), "template-approved.html"));
    }

    @Override
    public void sendNewCancelledTicketMail(Long ticketId) {
        User recipient = userDAO.getCreatorTicket(ticketId);
        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was cancelled", Map.of(
                "ticketId", ticketId,
                "username", recipient.getFirstname(),
                "userSurname", recipient.getLastname(),
                "baseUrl", baseUrl
        ), "template-new-cancelled.html");
    }

    @Override
    public void sendDeclinedTicketMail(Long ticketId) {
        User recipient = userDAO.getCreatorTicket(ticketId);
        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was declined", Map.of(
                "ticketId", ticketId,
                "username", recipient.getFirstname(),
                "userSurname", recipient.getLastname(),
                "baseUrl", baseUrl
        ), "template-declined.html");
    }

    @Override
    public void sendApprovedCancelledTicketMail(Long ticketId) {
        List<User> recipients = new ArrayList<>();
        recipients.add(userDAO.getCreatorTicket(ticketId));
        recipients.add(userDAO.getApproverTicket(ticketId));

        recipients.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was cancelled", Map.of(
                "ticketId", ticketId,
                "baseUrl", baseUrl
        ), "template-approved-cancelled.html"));

    }

    @Override
    public void sendDoneTicketMail(Long ticketId) {
        User recipient = userDAO.getCreatorTicket(ticketId);
        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was done", Map.of(
                "ticketId", ticketId,
                "username", recipient.getFirstname(),
                "userSurname", recipient.getLastname(),
                "baseUrl", baseUrl
        ), "template-done.html");
    }

    @Override
    public void sendFeedback(Long ticketId) {
        User recipient = userDAO.getEngineerForFeedback(ticketId);
        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Feedback was provided", Map.of(
                "ticketId", ticketId,
                "username", recipient.getFirstname(),
                "userSurname", recipient.getLastname(),
                "baseUrl", baseUrl
        ), "template-feedback.html");
    }
}
