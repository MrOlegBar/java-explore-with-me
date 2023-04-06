package ru.practicum.explore.with.me.stat.server.service;

import ru.practicum.explore.with.me.stat.server.Stat;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatService {
    Stat create(Stat stat);

    Collection<Stat> getStats(LocalDateTime start, LocalDateTime end, Collection<URI> uris, Boolean unique);
}
