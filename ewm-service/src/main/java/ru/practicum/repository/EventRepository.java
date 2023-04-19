package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Event;

import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long> {
    Collection<Event> findAllByInitiator_Id(Long userId, Pageable pageable);
    Collection<Event> findAllByCategory_Id(Long catId);
    Event findEventByInitiator_IdAndId(Long userId, Long eventId);
}
