package com.project.eventros.services;

import com.project.eventros.domain.entities.QrCode;
import com.project.eventros.domain.entities.Ticket;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
