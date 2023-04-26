package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.event.Event;
import ru.practicum.service.event.EventService;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping("/admin/events")
    public Collection<EventDto> getEvents(@RequestParam(required = false) Collection<Long> users,
                                          @RequestParam(required = false) Collection<Event.EventStatus> states,
                                          @RequestParam(required = false) Collection<Category> categories,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {

        Collection<Event> eventCollectionForDto = eventService.getEventsByFilter(users, states, categories, rangeStart,
                rangeEnd, from, size);
        return EventMapper.toEventDtoList(eventCollectionForDto);
    }
}
