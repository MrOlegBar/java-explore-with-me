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

import java.util.Objects;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String message = String.valueOf(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = String.format("Отсутствует параметр запроса %s.", ex.getParameterName());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String message = String.format("Отсутствует HttpInputMessage %s.", ex.getHttpInputMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({NumberFormatException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(RuntimeException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(RuntimeException e) {
        return new ApiError(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }

    @ExceptionHandler({org.hibernate.exception.ConstraintViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(RuntimeException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage());
    }
}