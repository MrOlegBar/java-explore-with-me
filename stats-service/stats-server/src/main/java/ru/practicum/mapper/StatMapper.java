package ru.practicum.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatShortDto;
import ru.practicum.model.Stat;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class StatMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public StatMapper() {
    }

    public static Stat toStat(StatDto statDto) {
        return modelMapper.map(Objects.requireNonNull(statDto), Stat.class);
    }

    public static StatDto toStatDto(Stat stat) {
        return modelMapper.map(Objects.requireNonNull(stat), StatDto.class);
    }

    public static StatShortDto toStatShortDto(Stat stat) {
        return modelMapper.map(Objects.requireNonNull(stat), StatShortDto.class);
    }

    public static Collection<StatShortDto> toStatShortDtoList(Collection<Stat> userCollection) {
        return userCollection
                .stream()
                .map(StatMapper::toStatShortDto)
                .collect(Collectors.toList());
    }
}