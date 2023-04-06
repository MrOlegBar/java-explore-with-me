package ru.practicum.explore.with.me.stat.server.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class StatShortDto {
    private String app;
    private String uri;
    private Long hits;
}