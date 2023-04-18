package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public Event create(Event event) {
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
    public Collection<Event> getEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(long eventId) {
        return eventRepository.findById(eventId);
    }
}
