package ru.practicum.service;

import ru.practicum.model.Main;

import java.util.Collection;
import java.util.Optional;

public interface MainService {
    Collection<Main> getEvents();

    Optional<Main> getEventById(long eventId);
}
