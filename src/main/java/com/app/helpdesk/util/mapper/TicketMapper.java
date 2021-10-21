package com.app.helpdesk.util.mapper;

import com.app.helpdesk.dto.CategoryAndUrgencyDto;
import com.app.helpdesk.dto.TicketDto;
import com.app.helpdesk.model.Ticket;
import com.app.helpdesk.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketMapper {
    List<TicketDto> mapToDto(List<Ticket> tickets);

    TicketDto mapToDraftDto(Ticket ticket, CategoryAndUrgencyDto categoryAndUrgencyDto);

    TicketDto mapToDto(Ticket ticket);

    Ticket mapToEntity(TicketDto ticketDto, MultipartFile file, User user, String draft);
}
