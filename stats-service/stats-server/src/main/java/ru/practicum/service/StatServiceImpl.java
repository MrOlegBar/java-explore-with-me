package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.repository.StatRepository;
import ru.practicum.model.Stat;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public Stat create(Stat stat) {
        return statRepository.save(stat);
    }

    @Override
    public Collection<Stat> getStats(LocalDateTime start, LocalDateTime end, Collection<URI> uris, Boolean unique) {
        Collection<Stat> result;

        if (uris != null) {
            if (unique) {
                result = statRepository.findByUriInAndDistinctIpAndTimestampBetween(uris, start, end);
            } else {
                result = statRepository.findByUriInAndTimestampBetween(uris, start, end);
            }
        } else {
            if (unique) {
                result = statRepository.findByDistinctIpAndTimestampBetween(start, end);
            } else {
                result = statRepository.findByTimestampBetween(start, end);
            }
        }

        return result;
    }
}