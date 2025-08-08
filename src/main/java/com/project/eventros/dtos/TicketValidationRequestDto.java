package com.project.eventros.dtos;

import com.project.eventros.entities.TicketValidationMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidationRequestDto {
    private UUID id;
    private TicketValidationMethod method;
}
