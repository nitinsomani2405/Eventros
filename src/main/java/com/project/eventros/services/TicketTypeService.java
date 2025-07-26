package com.project.eventros.services;

import com.project.eventros.domain.entities.Ticket;
import com.project.eventros.domain.entities.TicketType;

import java.util.UUID;

public interface TicketTypeService {

    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
