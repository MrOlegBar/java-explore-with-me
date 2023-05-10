package ru.practicum.controller.publicc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatClient;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final StatClient statClient;

    @GetMapping("/events")
    public Collection<ShortEventDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Collection<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) Event.EventSort sort,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size,
                                               HttpServletRequest request) throws NotFoundException {
        postStat(URI.create(request.getRequestURI()), request.getRemoteAddr(), LocalDateTime.now());

        if (text == null && categories == null && paid == null && rangeStart == null && rangeEnd == null && sort == null) {
            return new ArrayList<>();
        }

        if (rangeEnd == null || rangeStart == null) {
            rangeEnd = null;
            rangeStart = LocalDateTime.now();
        }

        Collection<Event> eventCollectionForDto = eventService.getEventsByPublicFilter(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);

        for (Event event : eventCollectionForDto) {
            event.setViews(event.getViews() + 1L);
        }

        eventService.save(eventCollectionForDto);

        return eventMapper.toShortEventDtoList(eventCollectionForDto);
    }

    @GetMapping("/events/{id}")
    public EventDto getEvent(@PathVariable("id") @NotNull Long eventId,
                             HttpServletRequest request) throws NotFoundException {
        postStat(URI.create(request.getRequestURI()), request.getRemoteAddr(), LocalDateTime.now());

        Event event = eventService.getEventByIdOrElseThrow(eventId);

        if (event.getState().equals(Event.EventStatus.PUBLISHED)) {
            event.setViews(event.getViews() + 1L);
            Event eventForDto = eventService.save(event);
            return eventMapper.toEventDto(eventForDto);
        } else {
            log.debug("Событие с eventId = {} не найдено.", eventId);
            throw new ConflictException(String.format("Событие с eventId = %s не найдено.",
                    eventId));
        }
    }

    private void postStat(URI uri, String ip, LocalDateTime localDateTime) {
        StatDto statDto = new StatDto();

        statDto.setApp("ewm-main-service");
        statDto.setUri(uri);
        statDto.setIp(ip);
        statDto.setTimestamp(localDateTime);

        statClient.postStat(statDto);
    }
}