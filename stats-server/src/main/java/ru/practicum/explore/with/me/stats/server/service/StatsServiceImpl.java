package ru.practicum.explore.with.me.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.stats.server.Stats;
import ru.practicum.explore.with.me.stats.server.StatsRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public Stats create(Stats stats) {
        return statsRepository.save(stats);
    }

    @Override
    public Collection<Stats> getStats(LocalDateTime start, LocalDateTime end, Collection<URI> uris, Boolean unique) {
        if (unique) {
            return statsRepository.findDistinctByUriInAndTimestampBetweenAndGroupByIp(uris, start, end);
        }
        Collection<Stats> result = statsRepository.findDistinctByUriInAndTimestampBetween(uris, start, end);
        result.forEach(stats -> {
                    long hits = result.stream()
                            .filter(stats1 -> stats1.getUri().equals(stats.getUri()))
                            .count();

                    stats.setHits(hits);
                });



        return result;
    }
}
