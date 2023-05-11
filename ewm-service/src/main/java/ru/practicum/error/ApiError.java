package ru.practicum.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}