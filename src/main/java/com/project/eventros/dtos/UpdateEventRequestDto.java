package com.project.eventros.dtos;

import com.project.eventros.entities.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequestDto {

    @NotNull(message = "Event Id must be provided")
    private UUID id;

    @NotBlank(message = "event name is required")
    private String name;

    private LocalDateTime start;
    private LocalDateTime end;

    @NotBlank(message = "venue info is required")
    private String venue;

    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;

    @NotNull(message = "status is required")
    private EventStatusEnum status;

    @NotEmpty(message = "atleast one ticket type is required")
    @Valid
    private List<UpdateTicketTypeRequestDto>ticketTypes;
}
