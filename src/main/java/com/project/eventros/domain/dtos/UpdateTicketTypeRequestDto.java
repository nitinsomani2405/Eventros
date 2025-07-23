package com.project.eventros.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketTypeRequestDto {

    private UUID id;

    @NotBlank(message = "ticket type name required")
    private String name;

    @NotNull(message = "price is required")
    @PositiveOrZero(message = "price must be zero or positive")
    private Double price;

    private String description;

    private Integer totalAvailable;
}
