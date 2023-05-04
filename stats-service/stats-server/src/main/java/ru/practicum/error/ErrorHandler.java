package ru.practicum.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();

        for (StackTraceElement error : stackTraceElements) {
            errors.add(error.toString());
        }

        String message = String.valueOf(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, message, errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NumberFormatException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(NumberFormatException e) {
        List<String> errors = new ArrayList<>();
        StackTraceElement[] stackTraceElements = e.getStackTrace();

        for (StackTraceElement error : stackTraceElements) {
            errors.add(error.toString());
        }

        return new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage(), errors);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();

        for (StackTraceElement error : stackTraceElements) {
            errors.add(error.toString());
        }

        String message = String.format("Отсутствует параметр запроса %s.", ex.getParameterName());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, message, errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        StackTraceElement[] stackTraceElements = ex.getStackTrace();

        for (StackTraceElement error : stackTraceElements) {
            errors.add(error.toString());
        }

        String message = String.format("Отсутствует HttpInputMessage %s.", ex.getHttpInputMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, message, errors);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(RuntimeException e) {
        List<String> errors = new ArrayList<>();
        StackTraceElement[] stackTraceElements = e.getStackTrace();

        for (StackTraceElement error : stackTraceElements) {
            errors.add(error.toString());
        }

        return new ApiError(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e.getMessage(), errors);
    }

    @ExceptionHandler({org.hibernate.exception.ConstraintViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(RuntimeException e) {
        List<String> errors = new ArrayList<>();
        StackTraceElement[] stackTraceElements = e.getStackTrace();

        for (StackTraceElement error : stackTraceElements) {
            errors.add(error.toString());
        }

        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), errors);
    }
}