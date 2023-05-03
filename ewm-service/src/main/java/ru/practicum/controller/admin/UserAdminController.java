package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constraintGroup.Post;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.model.User;
import ru.practicum.service.user.UserService;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {
    private final UserService userService;

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Validated({Post.class}) @RequestBody NewUserDto newUserDto) {
        User userFromDto = UserMapper.toUser(newUserDto);
        User userForDto = userService.save(userFromDto);

        return UserMapper.toUserDto(userForDto);
    }

    @GetMapping("/admin/users")
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        Collection<User> userCollectionForDto = userService.getUsers(ids, from, size);
        return UserMapper.toUserDtoList(userCollectionForDto);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable @NotNull Long userId) {
        userService.getUserByIdOrElseThrow(userId);

        return userService.deleteUser(userId);
    }
}
