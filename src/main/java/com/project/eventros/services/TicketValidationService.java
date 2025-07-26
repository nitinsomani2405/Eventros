package com.project.eventros.services;

import com.project.eventros.domain.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {
    TicketValidation validateTicketByQrCode(UUID qrCodeId);

    TicketValidation validateTicketManually(UUID ticketId);
}
