package ru.practicum.controller.publicc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatClient;
import ru.practicum.dto.StatDto;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    private final StatClient statClient;

    @GetMapping(value = {"/events", "/events/{eventId}"})
    public Object getEventS(@PathVariable(required = false) Long eventId, HttpServletRequest request) {
        StatDto statDto = new StatDto();
        statDto.setApp("ewm-main-service");
        statDto.setUri(URI.create(request.getRequestURI()));
        statDto.setIp(request.getRemoteAddr());
        statDto.setTimestamp(LocalDateTime.now());

        statClient.postStat(statDto);

        if (eventId == null) {
            return eventService.getEvents();
        } else {
            return eventService.getEventByIdOrElseThrow(eventId);
        }
    }
}