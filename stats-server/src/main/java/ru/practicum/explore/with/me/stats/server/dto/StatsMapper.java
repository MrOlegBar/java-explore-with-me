package ru.practicum.explore.with.me.stats.server.dto;

import org.springframework.stereotype.Component;
import ru.practicum.explore.with.me.stats.server.Stats;

@Component
public class StatsMapper {
    public static Stats toStats(StatsRequestDto statsRequestDto) {
        Stats stats = new Stats();
        stats.setApp(statsRequestDto.getApp());
        stats.setUri(statsRequestDto.getUri());
        stats.setIp(statsRequestDto.getIp());
        stats.setTimestamp(statsRequestDto.getTimestamp());
        return stats;
    }

    public static StatsResponseDto toStatsResponseDto(Stats stats) {
        return StatsResponseDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }
}