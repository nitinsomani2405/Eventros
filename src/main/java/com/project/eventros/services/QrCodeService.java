package com.project.eventros.services;

import com.project.eventros.entities.QrCode;
import com.project.eventros.entities.Ticket;

import java.util.UUID;


public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
