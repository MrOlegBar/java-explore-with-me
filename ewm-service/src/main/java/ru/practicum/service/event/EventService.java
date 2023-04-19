package ru.practicum.service.event;

import ru.practicum.error.NotFoundException;
import ru.practicum.model.event.Event;

import java.util.Collection;

public interface EventService {
    Event save(Event event);

    Event getEventByIdOrElseThrow(long eventId) throws NotFoundException;

    Collection<Event> getEventsByUserId(Long userId, int from, int size);

    Collection<Event> getEventsByCategoryId(Long catId);

    Collection<Event> getEvents();

    Event getEventByUserIdAndEventId(Long userId, Long eventId);
}
