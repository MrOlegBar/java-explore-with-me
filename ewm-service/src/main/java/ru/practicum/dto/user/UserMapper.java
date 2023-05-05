package ru.practicum.dto.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public UserMapper() {
        modelMapper = new ModelMapper();
    }

    public static User toUser(NewUserDto newUserDto) {
        return modelMapper.map(Objects.requireNonNull(newUserDto), User.class);
    }

    public static UserDto toUserDto(User user) {
        return modelMapper.map(Objects.requireNonNull(user), UserDto.class);
    }

    public static List<UserDto> toUserDtoList(Collection<User> userCollection) {
        return userCollection
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}