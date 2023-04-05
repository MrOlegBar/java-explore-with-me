package ru.practicum.explore.with.me.stats.server.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class StatsShortDto {
    private String app;
    private String uri;
    private Long hits;
}