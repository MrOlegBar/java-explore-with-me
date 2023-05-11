package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatShortDto;
import ru.practicum.model.Stat;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public Stat toStat(StatDto statDto) {
        return modelMapper.map(Objects.requireNonNull(statDto), Stat.class);
    }

    public StatDto toStatDto(Stat stat) {
        return modelMapper.map(Objects.requireNonNull(stat), StatDto.class);
    }

    public StatShortDto toStatShortDto(Stat stat) {
        return modelMapper.map(Objects.requireNonNull(stat), StatShortDto.class);
    }

    public Collection<StatShortDto> toStatShortDtoList(Collection<Stat> userCollection) {
        return userCollection
                .stream()
                .map(this::toStatShortDto)
                .collect(Collectors.toList());
    }
}