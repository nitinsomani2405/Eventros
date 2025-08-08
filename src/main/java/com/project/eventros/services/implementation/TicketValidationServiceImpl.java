package com.project.eventros.services.implementation;

import com.project.eventros.entities.*;
import com.project.eventros.exceptions.QrCodeNotFoundException;
import com.project.eventros.exceptions.TicketNotFoundException;
import com.project.eventros.respositories.QrCodeRepository;
import com.project.eventros.respositories.TicketRepository;
import com.project.eventros.respositories.TicketValidationRepository;
import com.project.eventros.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@Transactional
@RequiredArgsConstructor
public class TicketValidationServiceImpl implements TicketValidationService {
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeRepository qrCodeRepository;


    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(
                        ()->new QrCodeNotFoundException(
                                String.format("Qr code %s not found", qrCodeId)
                        )
                );

        Ticket ticket = qrCode.getTicket();

        return ValidateTicket(ticket, TicketValidationMethod.QR_SCAN);

    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket=ticketRepository.findById(ticketId).orElseThrow(
                TicketNotFoundException::new
        );
        return ValidateTicket(ticket,TicketValidationMethod.MANUAL);
    }

    private TicketValidation ValidateTicket(Ticket ticket,TicketValidationMethod ticketValidationMethod) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(ticketValidationMethod);

        TicketValidationStatusEnum ticketValidationStatusEnum=ticket.getValidations().stream()
                .filter(validation->TicketValidationStatusEnum.VALID.equals(validation.getStatus()))
                .findFirst()
                .map(validation->TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);
        ticketValidation.setStatus(ticketValidationStatusEnum);
        return ticketValidationRepository.save(ticketValidation);
    }
}
