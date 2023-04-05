package ru.practicum.explore.with.me.stats.server.dto;

import org.springframework.stereotype.Component;
import ru.practicum.explore.with.me.stats.server.Stats;

import java.net.URI;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static Stats toStats(StatsDto statsDto) {
        Stats stats = new Stats();
        stats.setApp(statsDto.getApp());
        stats.setUri(URI.create(statsDto.getUri()));
        stats.setIp(statsDto.getIp());
        stats.setTimestamp(stats.getTimestamp());
        return stats;
    }

    public static StatsDto toStatsDto(Stats stats) {
        return StatsDto.builder()
                .id(stats.getId())
                .app(stats.getApp())
                .uri(stats.getUri().toString())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp().format(formatter))
                .build();
    }
    public static StatsShortDto toStatsShortDto(Stats stats) {
        return StatsShortDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri().toString())
                .hits(stats.getHits())
                .build();
    }
}