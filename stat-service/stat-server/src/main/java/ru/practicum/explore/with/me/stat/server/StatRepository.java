package ru.practicum.explore.with.me.stat.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

public interface StatRepository extends JpaRepository<Stat, Long> {
    Collection<Stat> findDistinctByUriInAndTimestampBetween(Collection<URI> uri, LocalDateTime start,
                                                            LocalDateTime end);

    Collection<Stat> findDistinctByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT s FROM Stat s WHERE s.uri in ?1 and s.ip in (select distinct s.ip from Stat s) and s.timestamp between ?2 and ?3")
    Collection<Stat> findDistinctByUriInAndTimestampBetweenAndGroupByIp(Collection<URI> uri, LocalDateTime start,
                                                                        LocalDateTime end);

    @Query(value = "SELECT s FROM Stat s WHERE s.ip in (select distinct s.ip from Stat s) and s.timestamp between ?1 and ?2 group by s.id")
    Collection<Stat> findDistinctByTimestampBetweenAndGroupByIp(LocalDateTime start, LocalDateTime end);
}
