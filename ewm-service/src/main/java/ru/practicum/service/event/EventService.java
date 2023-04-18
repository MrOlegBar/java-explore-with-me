package ru.practicum.service.event;

import ru.practicum.error.NotFoundException;
import ru.practicum.model.Event;

import java.util.Collection;
import java.util.Optional;

public interface EventService {
    Event create(Event event);

    Event getEventByIdOrElseThrow(long eventId) throws NotFoundException;

    Collection<Event> getEvents();

    Optional<Event> getEventById(long eventId);
}
