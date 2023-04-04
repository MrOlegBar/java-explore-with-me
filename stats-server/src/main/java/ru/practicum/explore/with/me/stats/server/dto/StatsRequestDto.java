package ru.practicum.explore.with.me.stats.server.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;

@Builder
@Getter
public class StatsRequestDto {
    @NotBlank(message = "Идентификатор сервиса, для которого записывается информация, отсутствует или представлено " +
            "пустым символом.")
    private String app;
    @NotNull(message = "URI, для которого был осуществлен запрос, отсутствует.")
    private URI uri;
    @NotNull(message = "IP-адрес пользователя, осуществившего запрос, отсутствует.")
    private String ip;
    @NotNull(message = "Дата и время, когда был совершен запрос к эндпоинту, отсутствуют.")
    private LocalDateTime timestamp;
}
