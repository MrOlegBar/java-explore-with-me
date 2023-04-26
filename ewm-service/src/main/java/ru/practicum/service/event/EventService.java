package ru.practicum.service.event;

import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    Event save(Event event);

    Event getEventByIdOrElseThrow(long eventId) throws NotFoundException;

    Collection<Event> getEventsByUserId(Long userId, int from, int size);

    Collection<Event> getEventsByCategoryId(Long catId);

    Collection<Event> getEvents();

    Event getEventByUserIdAndEventId(Long userId, Long eventId);

    Collection<Event> getEventsByFilter(Collection<Long> users, Collection<Event.EventStatus> states,
                                        Collection<Category> categories, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, int from, int size);
}