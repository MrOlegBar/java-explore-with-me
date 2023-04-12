package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatClient;
import ru.practicum.dto.StatDto;
import ru.practicum.service.MainService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;
    private final StatClient statClient;

    @GetMapping(value = {"/events", "/events/{eventId}"})
    public Object getUserS(@PathVariable(required = false) Long eventId, HttpServletRequest request) {
        StatDto statDto = new StatDto();
        statDto.setApp("ewm-main-service");
        statDto.setUri(URI.create(request.getRequestURI()));
        statDto.setIp(request.getRemoteAddr());
        statDto.setTimestamp(LocalDateTime.now());

        statClient.postStat(statDto);

        if (eventId == null) {
            return mainService.getEvents();
        } else {
            return mainService.getEventById(eventId);
        }
    }
}
