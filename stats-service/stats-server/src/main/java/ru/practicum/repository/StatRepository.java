package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stat;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query(value = "SELECT new Stat(s.app, s.uri, count(DISTINCT s.ip)) FROM Stat s WHERE s.uri in ?1 and s.timestamp " +
            "between ?2 and ?3 group by s.app, s.uri")
    Collection<Stat> findByUriInAndDistinctIpAndTimestampBetween(Collection<URI> uri, LocalDateTime start,
                                                                 LocalDateTime end);

    @Query(value = "SELECT new Stat(s.app, s.uri, count(s.ip)) FROM Stat s WHERE s.uri in ?1 and s.timestamp " +
            "between ?2 and ?3 group by s.app, s.uri")
    Collection<Stat> findByUriInAndTimestampBetween(Collection<URI> uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new Stat(s.app, s.uri, count(distinct s.ip)) FROM Stat s WHERE s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri")
    Collection<Stat> findByDistinctIpAndTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT new Stat(s.app, s.uri, count(s.ip)) FROM Stat s WHERE s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri")
    Collection<Stat> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
