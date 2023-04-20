package ru.practicum.service.request;

import ru.practicum.error.NotFoundException;
import ru.practicum.model.Request;

import java.util.Collection;

public interface RequestService {
    Request save(Request request);

    Collection<Request> getRequestsByRequesterId(Long userId);

    Collection<Request> getRequestsByInitiatorId(Long userId, Long eventId);

    Collection<Request> getRequestsByCollectionId(Long userId, Long eventId, Collection<Long> requestIds);

    Request getRequestByIdOrElseThrow(long requestId) throws NotFoundException;
}
