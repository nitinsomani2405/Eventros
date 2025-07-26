package com.project.eventros.controllers;

import com.project.eventros.domain.dtos.TicketValidationRequestDto;
import com.project.eventros.domain.dtos.TicketValidationResponseDto;
import com.project.eventros.domain.entities.TicketValidation;
import com.project.eventros.domain.entities.TicketValidationMethod;
import com.project.eventros.mapper.TicketValidationMapper;
import com.project.eventros.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
    ) {
        TicketValidationMethod method=ticketValidationRequestDto.getMethod();
        TicketValidation validation;
        if(TicketValidationMethod.MANUAL.equals(method)){
            validation=ticketValidationService.validateTicketManually(
                    ticketValidationRequestDto.getId()
            );
        }else{
            validation=ticketValidationService.validateTicketByQrCode(
                    ticketValidationRequestDto.getId()
            );
        }
        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResponseDto(validation));
    }
}
