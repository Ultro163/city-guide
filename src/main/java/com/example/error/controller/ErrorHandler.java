package com.example.error.controller;

import com.example.error.exception.AccessDeniedException;
import com.example.error.exception.EntityNotFoundException;
import com.example.error.exception.ValidationException;
import com.example.error.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер обработки ошибок, который перехватывает исключения, происходящие в приложении, и возвращает
 * соответствующие ответы с информацией об ошибке.
 * Обрабатывает различные типы исключений, включая ошибки валидации, нарушения целостности данных,
 * ошибки доступа и другие.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final Exception e) {
        log.warn(e.getMessage());
        String errorMessage;
        List<String> errors = new ArrayList<>();
        String reason;

        if (e instanceof MethodArgumentNotValidException ex) {
            errors = ex.getBindingResult().getFieldErrors().stream()
                    .map(fieldError -> String.format("Field '%s': %s", fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());
            errorMessage = "Validation failed for one or more fields";
            reason = "MethodArgumentNotValidException";
        } else if (e instanceof MissingServletRequestParameterException ex) {
            errorMessage = String.format("Required parameter is missing: %s", ex.getParameterName());
            reason = "MissingServletRequestParameterException";
        } else {
            errorMessage = e.getMessage();
            reason = "ValidationException";
        }
        return ApiError.builder()
                .errors(errors)
                .message(errorMessage)
                .reason(reason)
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final EntityNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        return ApiError.builder()
                .errors(errors)
                .message(e.getMessage())
                .reason("EntityNotFoundException")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotFound(final DataIntegrityViolationException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        String errorMessage = "Data integrity violation occurred";
        String reason = "DataIntegrityViolationException";
        errors.add(e.getMostSpecificCause().getMessage());
        return ApiError.builder()
                .errors(errors)
                .message(errorMessage)
                .reason(reason)
                .status(HttpStatus.CONFLICT.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDeniedException(final AccessDeniedException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ApiError.builder()
                .errors(errors)
                .message("Access denied due to insufficient permissions")
                .reason("AccessDeniedException")
                .status(HttpStatus.FORBIDDEN.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}