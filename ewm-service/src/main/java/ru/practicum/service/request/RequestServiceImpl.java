package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.Request;
import ru.practicum.repository.RequestRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Override
    public Request save(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public Collection<Request> getRequestsByRequesterId(Long userId) {
        return requestRepository.findAllByRequester_Id(userId);
    }

    @Override
    public Collection<Request> getRequestsByInitiatorId(Long userId, Long eventId) {
        return requestRepository.findAllByEvent_Initiator_IdAndEvent_Id(userId, eventId);
    }

    @Override
    public Collection<Request> getRequestsByCollectionId(Long userId, Long eventId, Collection<Long> requestIds) {
        return requestRepository.findAllByEvent_Initiator_IdAndEvent_IdAndIdIn(userId, eventId, requestIds);
    }

    @Override
    public Request getRequestByIdOrElseThrow(long requestId) throws NotFoundException {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            log.debug("Запрос на участии в событии с requestId = {} не найден.", requestId);
            throw new NotFoundException(String.format("\"Запрос на участии в событии с requestId = %s не найден.",
                    requestId));
        });
    }
}
