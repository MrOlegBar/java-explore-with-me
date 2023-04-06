package ru.practicum.explore.with.me.stat.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.stat.server.Stat;
import ru.practicum.explore.with.me.stat.server.StatRepository;

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
                result = statRepository.findDistinctByUriInAndTimestampBetweenAndGroupByIp(uris, start, end);
            } else {
                result = statRepository.findDistinctByUriInAndTimestampBetween(uris, start, end);
            }
        } else {
            if (unique) {
                result = statRepository.findDistinctByTimestampBetweenAndGroupByIp(start, end);
            } else {
                result = statRepository.findDistinctByTimestampBetween(start, end);
            }
        }

        result.forEach(stat2 -> {
                    long hits = result.stream()
                            .filter(stat1 -> stat1.getUri().equals(stat2.getUri()))
                            .count();

                    stat2.setHits(hits);
                });
        return result;
    }
}
