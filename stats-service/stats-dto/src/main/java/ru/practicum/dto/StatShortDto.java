package ru.practicum.dto;

import lombok.*;

import java.net.URI;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class StatShortDto {
    private String app;
    private URI uri;
    private Long hits;
}