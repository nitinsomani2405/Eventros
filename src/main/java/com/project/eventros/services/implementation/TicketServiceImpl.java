package com.project.eventros.services.implementation;

import com.project.eventros.domain.entities.Ticket;
import com.project.eventros.respositories.TicketRepository;
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
    private final TicketRepository ticketRepository;
    @Override
    public Page<Ticket> listTicketsForUser(UUID ticketTypeId, Pageable pageable) {
        return ticketRepository.findByPurchaserId(ticketTypeId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID userId, UUID ticketId) {
        return ticketRepository.findByIdAndPurchaserId(ticketId, userId);
    }
}
