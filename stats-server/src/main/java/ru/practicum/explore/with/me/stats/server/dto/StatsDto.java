package ru.practicum.explore.with.me.stats.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class StatsDto {
    private Long id;
    @NotBlank(message = "Идентификатор сервиса, для которого записывается информация, отсутствует или представлено " +
            "пустым символом.")
    private String app;
    @NotNull(message = "URI, для которого был осуществлен запрос, отсутствует.")
    private String uri;
    @NotNull(message = "IP-адрес пользователя, осуществившего запрос, отсутствует.")
    private String ip;
    //@NotNull(message = "Дата и время, когда был совершен запрос к эндпоинту, отсутствуют.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;
}
