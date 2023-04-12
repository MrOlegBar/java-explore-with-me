package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Main;
import ru.practicum.repository.MainRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final MainRepository mainRepository;

    @Override
    public Collection<Main> getEvents() {
        return mainRepository.findAll();
    }

    @Override
    public Optional<Main> getEventById(long eventId) {
        return mainRepository.findById(eventId);
    }
}
