package com.project.eventros.controllers;

import com.project.eventros.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.project.eventros.domain.dtos.ListPublishedEventResponseDto;
import com.project.eventros.domain.entities.Event;
import com.project.eventros.mapper.EventMapper;
import com.project.eventros.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>>listPublishedEvents(
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        Page<Event>events;
        if (q != null && !q.trim().isEmpty()) {
            events = eventService.searchPublishedEvents(q, pageable);
        }
        else{
            events=eventService.listPublishedEvents(pageable);
        }
        return ResponseEntity.ok(events.map(event->
            eventMapper.toListPublishedEventResponseDto(event)
        ));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
            @PathVariable UUID eventId)
    {
        return eventService.getPublishedEvent(eventId)
                .map(event->eventMapper.toGetPublishedEventDetailsResponseDto(event))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
