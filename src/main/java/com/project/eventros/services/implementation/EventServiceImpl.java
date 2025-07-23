package com.project.eventros.services.implementation;

import com.project.eventros.domain.CreateEventRequest;
import com.project.eventros.domain.UpdateEventRequest;
import com.project.eventros.domain.UpdateTicketTypeRequest;
import com.project.eventros.domain.entities.Event;
import com.project.eventros.domain.entities.TicketType;
import com.project.eventros.domain.entities.User;
import com.project.eventros.exceptions.EventNotFoundException;
import com.project.eventros.exceptions.EventUpdateException;
import com.project.eventros.exceptions.TicketTypeNotFoundException;
import com.project.eventros.services.EventService;
import com.project.eventros.exceptions.UserNotFoundException;
import com.project.eventros.respositories.EventRepository;
import com.project.eventros.respositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer=userRepository.findById(organizerId)
                .orElseThrow(()->new UserNotFoundException(
                        "User with id " + organizerId + " not found"
                ));
        Event newEvent=new Event();

        List<TicketType>ticketTypes=event.getTicketTypes().stream().map(
                ticketType->{
                    TicketType ticketTypeToCreate=new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(newEvent);
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    return ticketTypeToCreate;
                }).toList();


        newEvent.setName(event.getName());
        newEvent.setStart(event.getStart());
        newEvent.setEnd(event.getEnd());
        newEvent.setVenue(event.getVenue());
        newEvent.setSalesStart(event.getSalesStart());
        newEvent.setSalesEnd(event.getSalesEnd());
        newEvent.setStatus(event.getStatus());
        newEvent.setOrganizer(organizer);
        newEvent.setTicketTypes(ticketTypes);

        return eventRepository.save(newEvent);

    }

    @Override
    public Page<Event> listEvents(UUID organizerId, Pageable pageable) {
       return eventRepository.findByOrganizerId(organizerId,pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId) {
        return eventRepository.findByIdAndOrganizerId(eventId,organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest event) {
        if(event.getId()==null){
            throw new EventUpdateException("Event ID cannot be null");
        }
        if(!eventId.equals(event.getId())){
            throw new EventUpdateException("Event ID does not match");
        }
        Event existingEvent = eventRepository.findByIdAndOrganizerId(eventId,organizerId)
                .orElseThrow(()->new EventNotFoundException(
                        String.format("Event with id %s not found", eventId)
                ));
        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        //put ticket ids in set of those event whose id is not null(previously added tickettypes)
        Set<UUID>requestTicketTypeIds= event.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        //removing those ticket types which are already in db but now we removed
        existingEvent.getTicketTypes().removeIf(existingTicketType->
                !requestTicketTypeIds.contains(existingTicketType.getId()));

        //after filteration we putting all ids in map
        Map<UUID,TicketType>existingTicketTypesIndex=existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, existingTicketType->existingTicketType));

        for(UpdateTicketTypeRequest ticketType:event.getTicketTypes()){
            if(ticketType.getId()==null){
                // Create
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);
            }else if(existingTicketTypesIndex.containsKey(ticketType.getId())){
                TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setDescription(ticketType.getDescription());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
            }
            else{
                throw new TicketTypeNotFoundException(
                        String.format("Ticket type with id %s not found", ticketType.getId())
                );
            }
        }

        return eventRepository.save(existingEvent);
    }
}
