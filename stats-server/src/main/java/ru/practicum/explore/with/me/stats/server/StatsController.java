package ru.practicum.explore.with.me.stats.server;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.stats.server.dto.StatsDto;
import ru.practicum.explore.with.me.stats.server.dto.StatsShortDto;
import ru.practicum.explore.with.me.stats.server.dto.StatsMapper;
import ru.practicum.explore.with.me.stats.server.service.StatsService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     * @param statsDto не может быть null.
     * @return сохраненную сущность; никогда не будет null.
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsDto postStats(@NonNull @RequestBody StatsDto statsDto) {
        Stats statsFromDto = StatsMapper.toStats(statsDto);

        Stats statsForDto = statsService.create(statsFromDto);
        return StatsMapper.toStatsDto(statsForDto);
    }

    /**
     * Получение статистики за выбранные даты по выбранному эндпоинту.
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику.
     * @param end Дата и время конца диапазона за который нужно выгрузить статистику.
     * @param uris Список uri для которых нужно выгрузить статистику.
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip).
     * @return список статистики или пустой список.
     */
    @GetMapping("/stats")
    public Collection<StatsShortDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                              @RequestParam Collection<URI> uris,
                                              @RequestParam(defaultValue = "false") Boolean unique) {

        return statsService.getStats(start, end, uris, unique).stream()
                .map(StatsMapper::toStatsShortDto)
                .collect(Collectors.toList());
    }
}
