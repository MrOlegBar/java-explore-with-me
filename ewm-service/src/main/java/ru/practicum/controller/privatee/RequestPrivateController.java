package ru.practicum.controller.privatee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.RequestStatus;
import ru.practicum.model.User;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;
import ru.practicum.service.user.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateController {
    private final RequestService requestService;
    private final UserService userService;
    private final EventService eventService;
    private final ModelMapper modelMapper;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto postRequest(@PathVariable Long userId,
                                  @RequestParam(required = false) Long eventId) {
        User requester = userService.getUserByIdOrElseThrow(userId);
        Event event = eventService.getEventByIdOrElseThrow(eventId);

        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setStatus(RequestStatus.PENDING);

        Request requestForDto = requestService.create(request);
        return modelMapper.map(requestForDto, RequestDto.class);
    }

    /*@GetMapping("/admin/users")
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {

        return userService.getUsers(ids, from, size).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }*/

    /*@DeleteMapping(value = "/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable Long userId) {
        userService.getUserByIdOrElseThrow(userId);

        return userService.deleteUser(userId);
    }*/
}
