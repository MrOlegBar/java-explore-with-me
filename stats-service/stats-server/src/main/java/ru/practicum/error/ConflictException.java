package ru.practicum.error;

public class ConflictException extends RuntimeException {
    public ConflictException(final String message) {
        super(message);
    }
}
