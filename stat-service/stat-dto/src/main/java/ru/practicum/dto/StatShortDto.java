package ru.practicum.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URI;

@Builder
@Getter
@EqualsAndHashCode
public class StatShortDto {
    private String app;
    private URI uri;
    private Long hits;
}