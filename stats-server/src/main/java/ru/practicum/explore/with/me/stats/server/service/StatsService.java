package ru.practicum.explore.with.me.stats.server.service;

import ru.practicum.explore.with.me.stats.server.Stats;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {
    Stats create(Stats stats);

    Collection<Stats> getStats(LocalDateTime start, LocalDateTime end, Collection<URI> uris, Boolean unique);
}
