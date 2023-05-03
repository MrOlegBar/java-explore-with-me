package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.event.Event;

import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Event findEventByInitiator_IdAndId(Long userId, Long eventId);

    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Collection<Event> findAllByCategory_Id(Long catId);
}