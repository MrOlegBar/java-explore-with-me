package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.Main;
import ru.practicum.MainRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final MainRepository mainRepository;

    @Override
    public Collection<Main> getEvents() {
        return mainRepository.findAll();
    }
}
