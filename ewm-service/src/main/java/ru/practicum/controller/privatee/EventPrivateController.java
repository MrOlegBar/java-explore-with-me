package ru.practicum.controller.privatee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Patch;
import ru.practicum.constraintGroup.Post;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.dto.request.NewRequestStatusDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.dto.request.RequestStatusDto;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;
import ru.practicum.service.user.UserService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;
    private final UserService userService;
    private final RequestService requestService;
    private final RequestMapper requestMapper;
    private final EventMapper eventMapper;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto postEvent(@PathVariable @NotNull Long userId,
                              @Validated({Post.class}) @RequestBody NewEventDto newEventDto) {
        User initiator = userService.getUserByIdOrElseThrow(userId);

        if (newEventDto.getEventDate() != null && newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            log.debug("Дата события не может быть в прошлом времени.");
            throw new ConflictException("Дата события не может быть в прошлом времени.");
        }

        Event eventFromDto = eventMapper.toEvent(newEventDto);
        eventFromDto.setInitiator(initiator);

        Event eventForDto = eventService.save(eventFromDto);
        return eventMapper.toEventDto(eventForDto);
    }

    @GetMapping("/users/{userId}/events")
    public Collection<ShortEventDto> getEvents(@PathVariable @NotNull Long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        userService.getUserByIdOrElseThrow(userId);

        Collection<Event> eventCollectionForDto = eventService.getEventsByUserId(userId, from, size);
        return eventMapper.toShortEventDtoList(eventCollectionForDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEvent(@PathVariable @NotNull Long userId,
                             @PathVariable @NotNull Long eventId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        eventService.getEventByIdOrElseThrow(eventId);

        Event eventForDto = eventService.getEventByUserIdAndEventId(userId, eventId);
        return eventMapper.toEventDto(eventForDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto patchEvent(@PathVariable @NotNull Long userId,
                               @PathVariable @NotNull Long eventId,
                               @Validated({Patch.class}) @RequestBody NewEventDto newEventDto) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (newEventDto.getEventDate() != null && newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            log.debug("Дата события с eventId = {} не может быть в прошлом времени.", eventId);
            throw new ConflictException(String.format("Дата события с eventId = %s не может состояться в прошлом времени.",
                    eventId));
        }

        if (event.getState().equals(Event.EventStatus.CANCELED) || event.getState().equals(Event.EventStatus.PENDING)) {
            eventService.patchEvent(newEventDto, event);

            Event eventForDto = eventService.save(event);
            return eventMapper.toEventDto(eventForDto);
        } else {
            log.debug("Событие с eventId = {} не отклонено и не в состоянии ожидания.", eventId);
            throw new ConflictException(String.format("Событие с eventId = %s не отклонено и не в состоянии ожидания.",
                    eventId));
        }
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getRequests(@PathVariable @NotNull Long userId,
                                              @PathVariable @NotNull Long eventId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        eventService.getEventByIdOrElseThrow(eventId);

        Collection<Request> requestCollectionForDto = requestService.getRequestsByInitiatorId(userId, eventId);
        return requestMapper.toRequestDtoList(requestCollectionForDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public RequestStatusDto patchEventRequestStatus(@PathVariable @NotNull Long userId,
                                                    @PathVariable @NotNull Long eventId,
                                                    @Validated({Patch.class}) @RequestBody
                                                    NewRequestStatusDto newRequestStatusDto) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (event.getParticipantLimit() != 0L || event.getRequestModeration()) {

            if ((event.getConfirmedRequests() == 0L && event.getParticipantLimit() != 0L) ||
                    (event.getParticipantLimit() > event.getConfirmedRequests())) {

                for (Long id : newRequestStatusDto.getRequestIds()) {

                    Request request = requestService.getRequestByIdOrElseThrow(id);

                    if (request.getStatus().equals(Request.RequestStatus.PENDING)) {

                        request.setStatus(newRequestStatusDto.getStatus());
                        if (request.getStatus().equals(Request.RequestStatus.CONFIRMED)) {
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
                            request.setEvent(event);
                        }
                        requestService.save(request);
                    } else {
                        log.debug("Статус можно изменить только у заявок, находящихся в состоянии {}.",
                                Request.RequestStatus.PENDING);
                        throw new ConflictException(String.format("Статус можно изменить только у заявок, находящихся в " +
                                        "состоянии %s.", Request.RequestStatus.PENDING));
                    }
                }

                Collection<Request> requestCollectionForDto = requestService.getRequestsByCollectionId(userId, eventId,
                        newRequestStatusDto.getRequestIds());
                return requestMapper.toRequestStatusDto(requestCollectionForDto);
            } else {
                log.debug("Достигнут лимит по заявкам на событие с eventId = {}.", eventId);
                throw new ConflictException(String.format("Достигнут лимит по заявкам на событие с eventId = %s.",
                        eventId));
            }
        }

        Collection<Request> requestCollectionForDto = requestService.getRequestsByCollectionId(userId, eventId,
                newRequestStatusDto.getRequestIds());
        return requestMapper.toRequestStatusDto(requestCollectionForDto);
    }
}