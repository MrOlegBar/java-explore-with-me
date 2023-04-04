package ru.practicum.explore.with.me.stats.server;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.stats.server.dto.StatsRequestDto;
import ru.practicum.explore.with.me.stats.server.dto.StatsResponseDto;
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
     * @param statsRequestDto не может быть null.
     * @return сохраненную сущность; никогда не будет null.
     */
    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Информация сохранена")
    public StatsResponseDto postStats(@NonNull @RequestBody StatsRequestDto statsRequestDto) {
        Stats statsFromDto = StatsMapper.toStats(statsRequestDto);

        Stats statsForDto = statsService.create(statsFromDto);
        return StatsMapper.toStatsResponseDto(statsForDto);
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
    @ResponseStatus(value = HttpStatus.OK, reason = "Статистика собрана")
    public Collection<StatsResponseDto> getStats(@RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING,
                                                         pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                 @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING,
                                                         pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                 @RequestParam Collection<URI> uris,
                                                 @RequestParam(defaultValue = "false") Boolean unique) {

        return statsService.getStats(start, end, uris, unique).stream()
                .map(StatsMapper::toStatsResponseDto)
                .collect(Collectors.toList());
    }
}
