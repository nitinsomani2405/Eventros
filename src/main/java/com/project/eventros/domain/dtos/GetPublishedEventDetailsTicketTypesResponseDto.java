package com.project.eventros.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPublishedEventDetailsTicketTypesResponseDto {
    private UUID id;
    private String name;
    private Double price;
    private String description;
}
