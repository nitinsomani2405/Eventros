package com.project.eventros.dtos;

import com.project.eventros.entities.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTicketResponseDto {
    private UUID id;
    private TicketStatusEnum status;
    private Double price;
    private String description;
    private String  eventName;
    private String eventVenue;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

}
