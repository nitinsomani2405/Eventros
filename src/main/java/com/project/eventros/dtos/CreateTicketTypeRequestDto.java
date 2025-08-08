package com.project.eventros.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequestDto {

    @NotBlank(message = "ticket type name required")
    private String name;

    @NotNull(message = "price is required")
    @PositiveOrZero(message = "price must be zero or positive")
    private Double price;

    private String description;

    private Integer totalAvailable;
}
