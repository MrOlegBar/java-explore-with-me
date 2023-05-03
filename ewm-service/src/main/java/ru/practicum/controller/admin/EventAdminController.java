package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.service.event.EventService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {
    private final EventService eventService;

    @GetMapping("/admin/events")
    public Collection<EventDto> getEvents(@RequestParam(required = false) Collection<Long> users,
                                          @RequestParam(required = false) Collection<Event.EventStatus> states,
                                          @RequestParam(required = false) Collection<Long> categories,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        if (users == null && states == null && categories == null && rangeStart == null && rangeEnd == null) {
            return new ArrayList<>();
        }

        Collection<Event> eventCollectionForDto = eventService.getEventsByAdminFilter(users, states, categories, rangeStart,
                rangeEnd, from, size);
        return EventMapper.toEventDtoList(eventCollectionForDto);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto patchEvent(@PathVariable @NotNull Long eventId,
                               @Validated({Patch.class}) @RequestBody NewEventDto newEventDto) throws NotFoundException {

        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (event.getState().equals(Event.EventStatus.PENDING)) {
            if (newEventDto.getEventDate() != null && newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
                log.debug("Событие с eventId = {} не может состояться в прошлом времени.", eventId);
                throw new ConflictException(String.format("Событие с eventId = %s не может состояться в прошлом времени.",
                        eventId));
            }

            eventService.patchEvent(newEventDto, event);

            Event eventForDto = eventService.save(event);
            return EventMapper.toEventDto(eventForDto);
        } else {
            log.debug("Событие с eventId = {} не в состоянии ожидания.", eventId);
            throw new ConflictException(String.format("Событие с eventId = %s не в состоянии ожидания.",
                    eventId));
        }
    }
}