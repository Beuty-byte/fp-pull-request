package com.app.helpdesk.util.mapper.impl;

import com.app.helpdesk.dto.*;
import com.app.helpdesk.model.*;
import com.app.helpdesk.model.enums.State;
import com.app.helpdesk.model.enums.Urgency;
import com.app.helpdesk.service.CategoryService;
import com.app.helpdesk.util.mapper.TicketMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapperImpl implements TicketMapper {

    private final CategoryService categoryService;

    @Autowired
    public TicketMapperImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public List<TicketDto> mapToDto(List<Ticket> tickets) {
        return tickets.stream().map(ticket -> TicketDto.builder()
                        .id(ticket.getId())
                        .name(ticket.getName())
                        .resolutionDate(ticket.getDesiredResolutionDate())
                        .urgency(ticket.getUrgency().toString())
                        .status(ticket.getState())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto mapToDraftDto(Ticket ticket, CategoryDto categoryDto) {
        return TicketDto.builder()
                .categoryDto(categoryDto)
                .urgency(ticket.getUrgency().toString())
                .attachmentDto(ticket.getAttachment() != null ?
                        new AttachmentDto(ticket.getAttachment().getId(), ticket.getAttachment().getName()) : null)
                .name(ticket.getName())
                .category(ticket.getCategory().getName())
                .comment(ticket.getComments().get(0).getText())
                .description(ticket.getDescription())
                .resolutionDate(ticket.getDesiredResolutionDate())
                .urgency(ticket.getUrgency().toString())
                .build();
    }

    @Override
    public TicketDto mapToDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId())
                .name(ticket.getName())
                .resolutionDate(ticket.getDesiredResolutionDate())
                .urgency(ticket.getUrgency().toString())
                .status(ticket.getState())

                .createdOn(ticket.getCreatedOn())
                .category(ticket.getCategory().getName())
                .owner(ticket.getOwner().getFirstname() + " " + ticket.getOwner().getLastname())
                .approver(ticket.getApprover() != null ?
                        ticket.getApprover().getFirstname() + " " + ticket.getApprover().getLastname() : "Not assigned")
                .assignee(ticket.getAssignee() != null ?
                        ticket.getAssignee().getFirstname() + " " + ticket.getAssignee().getLastname() : "Not assigned")
                .attachment(ticket.getAttachment() != null ?
                        new AttachmentDto(ticket.getAttachment().getId(), ticket.getAttachment().getName()) : null)
                .description(ticket.getDescription())
                .histories(!ticket.getHistory().isEmpty() ?
                        historyToDto(ticket.getHistory()) : null)
                .comments(!ticket.getComments().isEmpty() ?
                        commentToDto(ticket.getComments()) : null)
                .build();
    }

    private List<CommentDto> commentToDto(List<Comment> comments) {
        return comments.stream().limit(5)
                .map(comment -> CommentDto.builder()
                        .date(comment.getDate())
                        .text(comment.getText())
                        .username(comment.getUser().getFirstname() + " " + comment.getUser().getLastname())
                        .build())
                .collect(Collectors.toList());
    }


    private List<HistoryDto> historyToDto(List<History> histories) {
        return histories.stream().limit(5)
                .map(history -> HistoryDto.builder()
                        .date(history.getDate())
                        .action(history.getAction())
                        .description(history.getDescription())
                        .username(history.getUser().getFirstname() + " " + history.getUser().getLastname())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public Ticket mapToEntity(TicketDto ticketDto, MultipartFile file, User user, String draft) {
        Ticket ticket = new Ticket();

        if (ticketDto.getId() != null) {
            ticket.setId(ticketDto.getId());
        }

        ticket.setName(ticketDto.getName());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setDesiredResolutionDate(ticketDto.getResolutionDate() == null
                ? LocalDate.now().plusDays(10) : ticketDto.getResolutionDate());
        ticket.setOwner(user);
        ticket.setUrgency(Urgency.valueOf(ticketDto.getUrgency().toUpperCase()));

        Comment comment = getCommentFromDto(ticketDto.getComment(), user);
        comment.setTicket(ticket);
        ticket.addComment(comment);

        if (file != null) {
            Attachment attachment = getAttachmentFromMultipartFile(file);
            attachment.setTicket(ticket);
            ticket.setAttachment(attachment);
        }

        ticket.setCategory(getCategoryFromDto(ticketDto));
        ticket.setState(draft == null ? State.NEW : State.DRAFT);
        return ticket;
    }

    private Category getCategoryFromDto(TicketDto ticketDto) {
        return categoryService.getCategoryByName(ticketDto.getCategory());
    }

    @SneakyThrows
    private Attachment getAttachmentFromMultipartFile(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setContentType(file.getContentType());
        attachment.setBlob(file.getBytes());
        return attachment;
    }

    private Comment getCommentFromDto(String text, User user) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(user);
        return comment;
    }
}
