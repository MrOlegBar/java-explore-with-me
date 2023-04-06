package ru.practicum.explore.with.me.stat.server.dto;

import org.springframework.stereotype.Component;
import ru.practicum.explore.with.me.stat.server.Stat;

import java.net.URI;
import java.time.format.DateTimeFormatter;

@Component
public class StatMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Stat toStat(StatDto statDto) {
        Stat stat = new Stat();
        stat.setApp(statDto.getApp());
        stat.setUri(URI.create(statDto.getUri()));
        stat.setIp(statDto.getIp());
        stat.setTimestamp(stat.getTimestamp());
        return stat;
    }

    public static StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .id(stat.getId())
                .app(stat.getApp())
                .uri(stat.getUri().toString())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp().format(formatter))
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