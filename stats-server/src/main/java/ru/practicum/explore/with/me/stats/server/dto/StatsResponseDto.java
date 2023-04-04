package ru.practicum.explore.with.me.stats.server.dto;

import lombok.Builder;

import java.net.URI;

@Builder
public class StatsResponseDto {
    private String app;
    private URI uri;
    private Long hits;
}