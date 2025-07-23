package com.project.eventros.services;

import com.project.eventros.domain.CreateEventRequest;
import com.project.eventros.domain.UpdateEventRequest;
import com.project.eventros.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    Page<Event>listEvents(UUID organizerId, Pageable pageable);

    Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId);

    Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest event);
}
