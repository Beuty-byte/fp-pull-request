package com.app.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketDtoWrapper {
    private List<TicketDto> ticketDtoList;
    private long amountTickets;
}
