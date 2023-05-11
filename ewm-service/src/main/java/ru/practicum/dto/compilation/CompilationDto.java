package ru.practicum.dto.compilation;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.event.ShortEventDto;

import java.util.Collection;

@Getter
@Setter
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private Collection<ShortEventDto> events;
}