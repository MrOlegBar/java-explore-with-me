package ru.practicum.explore.with.me.stat.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatDto {
    private Long id;
    @NotBlank(message = "Идентификатор сервиса, для которого записывается информация, отсутствует или представлено " +
            "пустым символом.")
    private String app;
    @NotNull(message = "URI, для которого был осуществлен запрос, отсутствует.")
    private URI uri;
    @NotNull(message = "IP-адрес пользователя, осуществившего запрос, отсутствует.")
    private String ip;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}