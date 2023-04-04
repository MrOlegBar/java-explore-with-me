package ru.practicum.explore.with.me.stats.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    Collection<Stats> findDistinctByUriInAndTimestampBetween(Collection<URI> uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT s FROM Stats s WHERE s.uri in ?1 and s.timestamp between ?2 and ?3 group by s.ip")
    Collection<Stats> findDistinctByUriInAndTimestampBetweenAndGroupByIp(Collection<URI> uri, LocalDateTime start,
                                                                         LocalDateTime end);
}
