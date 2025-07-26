package com.project.eventros.mapper;

import com.project.eventros.domain.CreateEventRequest;
import com.project.eventros.domain.CreateTicketTypeRequest;
import com.project.eventros.domain.UpdateEventRequest;
import com.project.eventros.domain.UpdateTicketTypeRequest;
import com.project.eventros.domain.dtos.*;
import com.project.eventros.domain.entities.Event;
import com.project.eventros.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventDto(Event event);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

    UpdateEventResponseDto toUpdateEventResponseDto(Event event);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

    GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);

    GetPublishedEventDetailsTicketTypesResponseDto toGetPublishedEventDetailsTicketTypesResponseDto(TicketType ticketType);
}
