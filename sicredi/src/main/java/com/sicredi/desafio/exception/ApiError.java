package com.sicredi.desafio.exception;

import org.springframework.http.HttpStatus;

public record ApiError(
        int status,
        String error,
        String message,
        String path,
        String timestamp
) {
    public static ApiError of(HttpStatus s, String msg, String path) {
        return new ApiError(s.value(), s.getReasonPhrase(), msg, path, java.time.OffsetDateTime.now().toString());
    }
}