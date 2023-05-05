package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatShortDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.service.StatService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     * @param statDto не может быть null.
     * @return сохраненную сущность; никогда не будет null.
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDto postStats(@RequestBody StatDto statDto) {
        Stat statFromDto = StatMapper.toStat(statDto);
        Stat statForDto = statService.create(statFromDto);
        return StatMapper.toStatDto(statForDto);
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
    public Collection<StatShortDto> getStats(@RequestParam
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                             @RequestParam
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                             @RequestParam(required = false) Collection<URI> uris,
                                             @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        Collection<Stat> statCollectionForDto = statService.getStats(start, end, uris, unique);
        return StatMapper.toStatShortDtoList(statCollectionForDto);
    }
}