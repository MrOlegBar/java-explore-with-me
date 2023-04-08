package ru.practicum.explore.with.me.stat.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;

@Builder
@Getter
public class StatDto {
    private Long id;
    @NotBlank(message = "Идентификатор сервиса, для которого записывается информация, отсутствует или представлено " +
            "пустым символом.")
    private String app;
    @NotNull(message = "URI, для которого был осуществлен запрос, отсутствует.")
    private URI uri;
    @NotNull(message = "IP-адрес пользователя, осуществившего запрос, отсутствует.")
    private String ip;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
