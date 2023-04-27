package ru.practicum.dto.compilation;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.model.Compilation;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class CompilationMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public CompilationMapper() {
        modelMapper = new ModelMapper();
    }

    Converter<Compilation, CompilationDto> toNewCompilationDtoCompilationConverter() {
        return mappingContext -> {
            Compilation compilation = mappingContext.getSource();
            CompilationDto compilationDto = mappingContext.getDestination();

            compilationDto.setEvents(EventMapper.toShortEventDtoList(compilation.getEvents()));
            return compilationDto;
        };
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Compilation.class, CompilationDto.class)
                .addMappings(modelMapper -> modelMapper.skip(CompilationDto::setEvents))
                .setPostConverter(toNewCompilationDtoCompilationConverter());
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return modelMapper.map(Objects.requireNonNull(newCompilationDto), Compilation.class);
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return modelMapper.map(Objects.requireNonNull(compilation), CompilationDto.class);
    }
}
