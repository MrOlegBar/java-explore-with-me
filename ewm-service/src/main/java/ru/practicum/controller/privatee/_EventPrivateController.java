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
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.dto.request.NewRequestStatusDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.dto.request.RequestStatusDto;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;
import ru.practicum.service.user.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class _EventPrivateController {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestService requestService;

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
    public Collection<ShortEventDto> getEvents(@PathVariable Long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        Collection<Event> eventCollectionForDto = eventService.getEventsByUserId(userId, from, size);
        return EventMapper.toShortEventDtoList(eventCollectionForDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEvent(@PathVariable Long userId,
                                          @PathVariable Long eventId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        eventService.getEventByIdOrElseThrow(eventId);

        Event eventForDto = eventService.getEventByUserIdAndEventId(userId, eventId);
        return EventMapper.toEventDto(eventForDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto patchEvent(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Validated({Patch.class}) @RequestBody NewEventDto newEventDto) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (event.getState().equals(Event.EventStatus.CANCELED) || event.getState().equals(Event.EventStatus.PENDING)) {
            updateEventByNewEventDto(newEventDto, event);

            Event eventForDto = eventService.save(event);
            return EventMapper.toEventDto(eventForDto);
        } else {
            log.debug("Событие с eventId = {} не отклонено и не в состоянии ожидания.", eventId);
            throw new ConflictException(String.format("Событие с eventId = %s не отклонено и не в состоянии ожидания.",
                    eventId));
        }
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getRequests(@PathVariable Long userId,
                                              @PathVariable Long eventId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        eventService.getEventByIdOrElseThrow(eventId);

        Collection<Request> requestCollectionForDto = requestService.getRequestsByInitiatorId(userId, eventId);
        return RequestMapper.toRequestDtoList(requestCollectionForDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public RequestStatusDto patchEventRequestStatus(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Validated({Patch.class}) @RequestBody
                                                    NewRequestStatusDto newRequestStatusDto) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (event.getParticipantLimit() != 0L || event.getRequestModeration()) {

            if (event.getParticipantLimit() > (event.getConfirmedRequests())) {

                for(Long id : newRequestStatusDto.getRequestIds()) {

                    Request request = requestService.getRequestByIdOrElseThrow(id);

                    if (request.getStatus().equals(Request.RequestStatus.PENDING)) {

                        request.setStatus(newRequestStatusDto.getStatus());
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
                return RequestMapper.toRequestStatusDto(requestCollectionForDto);
            } else {
                log.debug("Достигнут лимит по заявкам на событие с eventId = {}.", eventId);
                throw new ConflictException(String.format("Достигнут лимит по заявкам на событие с eventId = %s.",
                        eventId));
            }
        }

        Collection<Request> requestCollectionForDto = requestService.getRequestsByCollectionId(userId, eventId,
                newRequestStatusDto.getRequestIds());
        return RequestMapper.toRequestStatusDto(requestCollectionForDto);
    }

    private void updateEventByNewEventDto(NewEventDto newEventDto, Event event) {
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
}
