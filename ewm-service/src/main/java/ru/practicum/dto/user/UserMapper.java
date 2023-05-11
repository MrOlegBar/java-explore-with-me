package ru.practicum.dto.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toUser(NewUserDto newUserDto) {
        return modelMapper.map(Objects.requireNonNull(newUserDto), User.class);
    }

    public UserDto toUserDto(User user) {
        return modelMapper.map(Objects.requireNonNull(user), UserDto.class);
    }

    public List<UserDto> toUserDtoList(Collection<User> userCollection) {
        return userCollection
                .stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}