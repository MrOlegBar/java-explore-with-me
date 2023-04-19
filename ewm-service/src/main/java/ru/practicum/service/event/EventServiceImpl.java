package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.repository.EventRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event getEventByIdOrElseThrow(long eventId) throws NotFoundException {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.debug("Событие с eventId = {} не найдено.", eventId);
            throw new NotFoundException(String.format("Событие с eventId = %s не найдено.",
                    eventId));
        });
    }

    @Override
    public Collection<Event> getEventsByUserId(Long userId, int from, int size) {
        return eventRepository.findAllByInitiator_Id(userId, PageRequest.of(from, size));
    }

    @Override
    public Collection<Event> getEventsByCategoryId(Long catId) {
        return eventRepository.findAllByCategory_Id(catId);
    }

    @Override
    public Collection<Event> getEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventByUserIdAndEventId(Long userId, Long eventId) {
        return eventRepository.findEventByInitiator_IdAndId(userId, eventId);
    }
}
