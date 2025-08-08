package com.project.eventros.controllers;

import com.project.eventros.dtos.GetTicketResponseDto;
import com.project.eventros.dtos.ListTicketResponseDto;
import com.project.eventros.mapper.TicketMapper;
import com.project.eventros.services.QrCodeService;
import com.project.eventros.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;

    //all tickets for user
    @GetMapping
    public Page<ListTicketResponseDto> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ticketService.listTicketsForUser(userId, pageable)
                .map(ticket -> ticketMapper.toListTicketResponseDto(ticket));

    }

    //particular ticket of a user
    @GetMapping("/{ticketId}")
    public ResponseEntity<GetTicketResponseDto>getTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ticketService
                .getTicketForUser(userId, ticketId)
                .map(ticketMapper::toGetTicketResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{ticketId}/qr-codes")
    public ResponseEntity<byte[]>getTicketQrCode(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID ticketId
    ){
        UUID userId = UUID.fromString(jwt.getSubject());
        byte[]qrCodeImage=qrCodeService.getQrCodeImageForUserAndTicket(userId, ticketId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeImage.length);

        return ResponseEntity.ok().headers(headers).body(qrCodeImage);
    }
    public UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
