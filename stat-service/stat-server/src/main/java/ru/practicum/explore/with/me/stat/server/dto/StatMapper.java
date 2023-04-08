package ru.practicum.explore.with.me.stat.server.dto;

import org.springframework.stereotype.Component;
import ru.practicum.explore.with.me.stat.server.Stat;

@Component
public class StatMapper {

    public static Stat toStat(StatDto statDto) {
        Stat stat = new Stat();
        stat.setApp(statDto.getApp());
        stat.setUri(statDto.getUri());
        stat.setIp(statDto.getIp());
        stat.setTimestamp(stat.getTimestamp());
        return stat;
    }

    public static StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .id(stat.getId())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp())
                .build();
    }

    public static StatShortDto toStatShortDto(Stat stat) {
        return StatShortDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri().toString())
                .hits(stat.getHits())
                .build();
    }
}