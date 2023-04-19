package ru.practicum.controller.privatee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventStatus;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto postEvent(@PathVariable Long userId,
                              @RequestBody NewEventDto newEventDto) {
        User initiator = userService.getUserByIdOrElseThrow(userId);

        Event eventFromDto = EventMapper.toEvent(newEventDto);
        eventFromDto.setInitiator(initiator);

        Event eventForDto = eventService.save(eventFromDto);
        return EventMapper.toEventDto(eventForDto);
    }

    @GetMapping("/users/{userId}/events")
    public Collection<EventDto> getEvents(@PathVariable Long userId,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        Collection<Event> eventCollectionForDto = eventService.getEventsByUserId(userId, from, size);
        return EventMapper.toEventDtoList(eventCollectionForDto);
    }

    @GetMapping("/users/{userId}/events/{eventsId}")
    public EventDto getEvents(@PathVariable Long userId,
                              @PathVariable Long eventsId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        eventService.getEventByIdOrElseThrow(eventsId);

        Event eventForDto = eventService.getEventByUserIdAndEventId(userId, eventsId);
        return EventMapper.toEventDto(eventForDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto patchEvent(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Validated({Patch.class}) @RequestBody NewEventDto newEventDto) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (event.getState() == EventStatus.CANCELED || event.getState() == EventStatus.PENDING) {


            Event eventForDto = eventService.save(event);
            return EventMapper.toEventDto(eventForDto);
        } else {
            log.debug("Событие с eventId = {} не отклонено.", eventId);
            throw new ConflictException(String.format("Событие с eventId = %s не отклонено.",
                    eventId));
        }
    }
}
