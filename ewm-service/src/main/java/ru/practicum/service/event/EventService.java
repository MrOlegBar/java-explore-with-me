package ru.practicum.service.event;

import ru.practicum.dto.event.NewEventDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    Event save(Event event);

    Collection<Event> save(Collection<Event> events);

    Event getEventByIdOrElseThrow(long eventId) throws NotFoundException;

    Collection<Event> getEventsByUserId(Long userId, int from, int size);

    Collection<Event> getEventsByCategoryId(Long catId);

    Collection<Event> getEvents();

    Event getEventByUserIdAndEventId(Long userId, Long eventId);

    Collection<Event> getEventsByAdminFilter(Collection<Long> users, Collection<Event.EventStatus> states,
                                             Collection<Long> categories, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, int from, int size);

    void patchEvent(NewEventDto newEventDto, Event event);

    Collection<Event> getEventsByPublicFilter(String text, Collection<Long> categories, Boolean paid,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                              Event.EventSort sort, int from, int size);
}