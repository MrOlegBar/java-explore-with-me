package ru.practicum.explore.with.me.stats.server.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatsShortDto {
    private String app;
    private String uri;
    private Long hits;
}