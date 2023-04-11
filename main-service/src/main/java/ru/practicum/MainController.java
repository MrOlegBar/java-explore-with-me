package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatDto;
import ru.practicum.service.MainService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;
    private final StatClient statClient;

    @GetMapping("/events")
    public Collection<Main> getEvents(HttpServletRequest request) {
        StatDto statDto = new StatDto();
        statDto.setApp("ewm-main-service");
        statDto.setUri(URI.create(request.getRequestURI()));
        statDto.setIp(request.getRemoteAddr());
        statDto.setTimestamp(LocalDateTime.now());

        statClient.postStat(statDto);
        return mainService.getEvents();

    }
}
