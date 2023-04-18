package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;
import ru.practicum.service.user.UserService;
import ru.practicum.constraintGroup.Post;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Validated({Post.class}) @RequestBody UserDto userDto) {
        User userFromDto = modelMapper.map(userDto, User.class);
        User userForDto = userService.create(userFromDto);
        return modelMapper.map(userForDto, UserDto.class);
    }

    @GetMapping("/admin/users")
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {

        return userService.getUsers(ids, from, size).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable Long userId) {
        userService.getUserByIdOrElseThrow(userId);

        return userService.deleteUser(userId);
    }
}
