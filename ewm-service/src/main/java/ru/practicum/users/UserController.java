package ru.practicum.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.users.UserService.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@ResponseStatus(HttpStatus.CREATED)
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@NonNull @RequestBody UserDto userDto) {
        User userFromDto = modelMapper.map(userDto, User.class);
        User userForDto = userService.create(userFromDto);
        return modelMapper.map(userForDto, UserDto.class);
    }
}
