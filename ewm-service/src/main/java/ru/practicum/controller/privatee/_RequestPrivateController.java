package ru.practicum.controller.privatee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;
import ru.practicum.service.user.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class _RequestPrivateController {
    private final RequestService requestService;
    private final UserService userService;
    private final EventService eventService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto postRequest(@PathVariable Long userId,
                                  @RequestParam Long eventId) {
        Request request = new Request();
        User requester = userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        methodParametersValidation(event, userId, eventId);

        if (!event.getRequestModeration()) {
            request.setStatus(Request.RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1L);
        } else {
            request.setStatus(Request.RequestStatus.PENDING);
        }

        request.setRequester(requester);
        request.setEvent(event);

        Request requestForDto = requestService.save(request);
        return RequestMapper.toRequestDto(requestForDto);
    }

    private void methodParametersValidation(Event event, Long userId, Long eventId) {
        if (event.getInitiator().getId().equals(userId)) {
            log.debug("Инициатор события с userId = {} не может добавить запрос на участие в своём событии с " +
                    "eventId = {}.", userId, eventId);
            throw new ConflictException(String.format("Инициатор события с userId = %s не может добавить запрос на " +
                    "участие в своём событии с eventId = %s.", userId, eventId));
        }
        if (!event.getState().equals(Event.EventStatus.PUBLISHED)) {
            log.debug("Пользователь с userId = {} не может участвовать в неопубликованном событии с eventId = {}.",
                    userId, eventId);
            throw new ConflictException(String.format("Пользователь с userId = %s не может участвовать в " +
                    "неопубликованном событии с eventId = %s.", userId, eventId));
        }
        if (event.getParticipantLimit() == 0L) {
            log.debug("У события с eventId = {} достигнут лимит запросов на участие.", eventId);
            throw new ConflictException(String.format("У события с eventId = %s достигнут лимит запросов на участие.",
                    eventId));
        }
    }

    @GetMapping("/users/{userId}/requests")
    public Collection<RequestDto> getRequests(@PathVariable Long userId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);

        Collection<Request> requestCollectionForDto = requestService.getRequestsByRequesterId(userId);
        return RequestMapper.toRequestDtoList(requestCollectionForDto);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto patchRequest(@PathVariable Long userId,
                               @PathVariable Long requestId) throws NotFoundException {
        userService.getUserByIdOrElseThrow(userId);
        Request request = requestService.getRequestByIdOrElseThrow(requestId);

        if (request.getRequester().getId().equals(userId)) {
            request.setStatus(Request.RequestStatus.CANCELED);
            Request requestForDto = requestService.save(request);
            return RequestMapper.toRequestDto(requestForDto);
        } else {
            log.debug("Запрос с requestId = {} не доступен пользователю с userId = {}.", requestId, userId);
            throw new NotFoundException(String.format("Запрос с requestId = %s не доступен пользователю с userId = %s.",
                    requestId, userId));
        }
    }
}
