package com.sicredi.desafio.exception;

import com.sicredi.desafio.external.exception.CpfNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.warn("400 Validation msg={}", ex.getMessage());
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("Validation error");
        var body = ApiError.of(HttpStatus.BAD_REQUEST, msg, req.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessageKey(), null, locale);
        log.warn("404 Not Found msg={}", msg);
        var body = ApiError.of(HttpStatus.NOT_FOUND, msg, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(CpfNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(CpfNotFoundException ex, HttpServletRequest req) {
        log.warn("404 Invalid CPF msg={}", ex.getMessage());
        var body = ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NoResourceFoundException ex, HttpServletRequest req) {
        log.warn("404 Path Invalid msg={}", ex.getMessage());
        var body = ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("405 MethodNotSupported msg={}", ex.getMessage());
        var body = ApiError.of(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest req, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessageKey(), null, locale);
        log.warn("409 Conflict msg={}", msg);
        var body = ApiError.of(HttpStatus.CONFLICT, msg, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("409 DataIntegrity msg={}", ex.getMessage());
        var body = ApiError.of(HttpStatus.CONFLICT, "Duplicate value", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ApiError> on422(UnprocessableEntityException ex, HttpServletRequest req, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessageKey(), null, locale);
        log.warn("422 Unprocessable msg={}", msg);
        var body = ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, msg, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(UnableToVoteException.class)
    public ResponseEntity<ApiError> handleUnableToVote(UnableToVoteException ex, HttpServletRequest req, Locale locale) {
        String msg = messageSource.getMessage(ex.getMessage(), null, locale);
        log.warn("422 UnableToVote msg={}", msg);
        var body = ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, msg, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        var body = ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req.getRequestURI());
        log.warn("500 Unexpected msg={}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
