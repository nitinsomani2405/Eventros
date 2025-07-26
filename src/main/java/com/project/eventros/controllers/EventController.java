package com.project.eventros.controllers;


import com.project.eventros.domain.CreateEventRequest;
import com.project.eventros.domain.UpdateEventRequest;
import com.project.eventros.domain.dtos.*;
import com.project.eventros.domain.entities.Event;
import com.project.eventros.mapper.EventMapper;
import com.project.eventros.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto>createEvent(
            @AuthenticationPrincipal Jwt jwt
            ,@Valid @RequestBody CreateEventRequestDto createEventRequestDto
            ){

        CreateEventRequest createEventRequest=eventMapper.fromDto(createEventRequestDto);
        UUID userId = getUserId(jwt) ;
        Event event = eventService.createEvent(userId,createEventRequest);
        CreateEventResponseDto createEventResponseDto=eventMapper.toDto(event);


        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>>listEvents(
            @AuthenticationPrincipal Jwt jwt, Pageable pageable
    ){
        UUID userId = getUserId(jwt) ;
        Page<Event> events = eventService.listEvents(userId,pageable);
        return ResponseEntity.ok(
                events.map(eventMapper::toListEventDto)
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto>getEvent(
            @AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId
    ){
        UUID userId = getUserId(jwt) ;
        return eventService.getEventForOrganizer(userId,eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto
    ){
        UpdateEventRequest updateEventRequest=eventMapper.fromDto(updateEventRequestDto);
        UUID userId = getUserId(jwt) ;

        Event updatedevent = eventService.updateEventForOrganizer(userId,eventId,updateEventRequest);
        UpdateEventResponseDto updateEventResponseDto= eventMapper.toUpdateEventResponseDto(updatedevent);
        return new ResponseEntity<>(updateEventResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal Jwt jwt
            , @PathVariable UUID eventId
    ){
        UUID userId = getUserId(jwt) ;
        eventService.deleteEventForOrganizer(userId,eventId);
        return ResponseEntity.noContent().build();
    }

    public UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
