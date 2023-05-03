package ru.practicum.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
public class ApiError {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final Collection<String> errors;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(HttpStatus status, String reason, String message, Collection<String> errors) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.errors = errors;
    }
}