package ru.practicum.error;

import lombok.Getter;

@Getter
public class ApiError {
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }
}