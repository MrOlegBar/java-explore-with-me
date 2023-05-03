package ru.practicum.dto.compilation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.model.Compilation;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public CompilationMapper() {
        modelMapper = new ModelMapper();
    }

    /*Converter<Compilation, CompilationDto> toNewCompilationDtoCompilationConverter() {
        return mappingContext -> {
            Compilation compilation = mappingContext.getSource();
            CompilationDto compilationDto = mappingContext.getDestination();

            compilationDto.setEvents(EventMapper.toShortEventDtoList(compilation.getEvents()));
            return compilationDto;
        };
    }*/

    /*@PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Compilation.class, CompilationDto.class)
                .addMappings(modelMapper -> modelMapper.skip(CompilationDto::setEvents))
                .setPostConverter(toNewCompilationDtoCompilationConverter());
    }*/

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return modelMapper.map(Objects.requireNonNull(newCompilationDto), Compilation.class);
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
}
