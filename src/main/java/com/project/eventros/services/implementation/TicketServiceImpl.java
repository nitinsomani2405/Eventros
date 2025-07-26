package com.project.eventros.services.implementation;

import com.project.eventros.domain.entities.Ticket;
import com.project.eventros.respositories.TicketRespository;
import com.project.eventros.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRespository ticketRespository;
    @Override
    public Page<Ticket> listTicketsForUser(UUID ticketTypeId, Pageable pageable) {
        return ticketRespository.findByPurchaserId(ticketTypeId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID userId, UUID ticketId) {
        return ticketRespository.findByIdAndPurchaserId(ticketId, userId);
    }
}
