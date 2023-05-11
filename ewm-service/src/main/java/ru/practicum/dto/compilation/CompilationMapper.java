package ru.practicum.dto.compilation;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.EventRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;

    public CompilationDto toCompilationDto(Compilation compilation) {
        return modelMapper.map(Objects.requireNonNull(compilation), CompilationDto.class);
    }

    public Collection<CompilationDto> toCompilationDtoList(Collection<Compilation> compilationCollection) {
        return compilationCollection
                .stream()
                .map(this::toCompilationDto)
                .collect(Collectors.toList());
    }

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = modelMapper.map(Objects.requireNonNull(newCompilationDto), Compilation.class);
        Set<Event> events = new HashSet<>();

        if (newCompilationDto.getEvents() != null) {
            for (Long id : newCompilationDto.getEvents()) {
                events.add(eventRepository.findById(id).orElse(null));
            }
        }
        compilation.setEvents(events);
        return compilation;
    }
}