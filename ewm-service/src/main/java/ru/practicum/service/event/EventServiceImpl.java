package ru.practicum.service.event;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.config.QPredicates;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.event.Event;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.category.CategoryService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import static ru.practicum.model.event.QEvent.event;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryService categoryService;

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

    @Override
    public Collection<Event> getEventsByAdminFilter(Collection<Long> users, Collection<Event.EventStatus> states,
                                                    Collection<Long> categories, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, int from, int size) {
        Predicate predicate = QPredicates.builder()
                .add(users, event.initiator.id::in)
                .add(states, event.state::in)
                .add(categories, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .buildAnd();

        return eventRepository.findAll(predicate, PageRequest.of(from, size)).getContent();
    }

    @Override
    public void patchEvent(NewEventDto newEventDto, Event event) {
        if (newEventDto.getAnnotation() != null) {
            event.setAnnotation(newEventDto.getAnnotation());
        }
        if (newEventDto.getCategory() != null || newEventDto.getCategory() != 0L) {
            Category category = categoryService.getCategoryByIdOrElseThrow(newEventDto.getCategory());
            event.setCategory(category);
        }
        if (newEventDto.getDescription() != null) {
            event.setDescription(newEventDto.getDescription());
        }
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(newEventDto.getEventDate());
        }
        if (newEventDto.getLocation() != null) {
            event.setLocation(newEventDto.getLocation());
        }
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        if (newEventDto.getTitle() != null) {
            event.setTitle(newEventDto.getTitle());
        }
        if (newEventDto.getStateAction() == NewEventDto.StateAction.SEND_TO_REVIEW) {
            event.setState(Event.EventStatus.PENDING);
        } else {
            event.setState(Event.EventStatus.PENDING);
        }
    }

    @Override
    public Collection<Event> getEventsByPublicFilter(String text, Collection<Long> categories, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                     Event.EventSort sort, int from, int size) {

        Predicate predicate = QPredicates.builder()
                .add(text, txt -> event.annotation.containsIgnoreCase(txt).or(event.description.containsIgnoreCase(txt)))
                .add(categories, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .add(paid, event.paid::eq)
                .add(Event.EventStatus.PUBLISHED, event.state::eq)
                .buildAnd();

        Collection<Event> events = eventRepository.findAll(predicate, PageRequest.of(from, size)).getContent();

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() > (event.getConfirmedRequests()))
                    .collect(Collectors.toList());
        }
        if (sort.equals(Event.EventSort.EVENT_DATE)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        }
        if (sort.equals(Event.EventSort.VIEWS)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getViews))
                    .collect(Collectors.toList());
        }
        return events;
    }
}