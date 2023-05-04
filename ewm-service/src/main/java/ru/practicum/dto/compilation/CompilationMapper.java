package ru.practicum.dto.compilation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CompilationMapper {
    private static ModelMapper modelMapper;
    private static EventRepository eventRepository;

    @Autowired
    public CompilationMapper(EventRepository eventRepository) {
        modelMapper = new ModelMapper();
        CompilationMapper.eventRepository = eventRepository;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return modelMapper.map(Objects.requireNonNull(compilation), CompilationDto.class);
    }

    public static Collection<CompilationDto> toCompilationDtoList(Collection<Compilation> compilationCollection) {
        return compilationCollection
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
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