package ru.practicum.controller.privatee;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.model.Event;
import ru.practicum.service.event.EventService;

@RestController
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;
    private final ModelMapper modelMapper;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId,
                                  @RequestBody EventDto eventDto) {
        Event eventFromDto = modelMapper.map(eventDto, Event.class);

        Event eventForDto = eventService.create(eventFromDto);
        return modelMapper.map(eventForDto, EventFullDto.class);
    }
}
