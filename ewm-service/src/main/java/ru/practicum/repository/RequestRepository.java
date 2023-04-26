package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findAllByRequester_Id(Long userId);

    Collection<Request> findAllByEvent_Initiator_IdAndEvent_Id(Long userId, Long eventId);

    Collection<Request> findAllByEvent_Initiator_IdAndEvent_IdAndIdIn(Long userId, Long eventId, Collection<Long> requestIds);
}