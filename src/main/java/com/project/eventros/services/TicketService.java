package com.project.eventros.services;


import com.project.eventros.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    Page<Ticket> listTicketsForUser(UUID ticketTypeId, Pageable pageable);

    Optional<Ticket> getTicketForUser(UUID userId,UUID ticketId);
}
