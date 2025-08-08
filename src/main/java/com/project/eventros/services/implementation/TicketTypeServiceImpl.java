package com.project.eventros.services.implementation;

import com.project.eventros.entities.Ticket;
import com.project.eventros.entities.TicketStatusEnum;
import com.project.eventros.entities.TicketType;
import com.project.eventros.entities.User;
import com.project.eventros.exceptions.TicketSoldOutException;
import com.project.eventros.exceptions.TicketTypeNotFoundException;
import com.project.eventros.exceptions.UserNotFoundException;
import com.project.eventros.respositories.TicketRepository;
import com.project.eventros.respositories.TicketTypeRespository;
import com.project.eventros.respositories.UserRepository;
import com.project.eventros.services.QrCodeService;
import com.project.eventros.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRespository ticketTypeRespository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;


    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user=userRepository.findById(userId).orElseThrow(
                ()->new UserNotFoundException(
                        String.format("User with Id"+ userId+"not found")
                )
        );

        TicketType ticketType=ticketTypeRespository.findByIdWithLock(ticketTypeId).orElseThrow(
                ()->new TicketTypeNotFoundException(
                        String.format("Ticket type with Id"+ ticketTypeId+"not found")
                )
        );
        int purchasedTickets= ticketRepository.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable = ticketType.getTotalAvailable();
        if(purchasedTickets+1>totalAvailable){
            throw new TicketSoldOutException();
        }
        Ticket ticket=new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);
        Ticket savedTicket= ticketRepository.save(ticket);

        qrCodeService.generateQrCode(savedTicket);
        return ticketRepository.save(savedTicket);
    }
}
