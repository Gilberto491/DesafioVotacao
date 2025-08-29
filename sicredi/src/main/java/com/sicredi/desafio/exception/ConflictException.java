package com.sicredi.desafio.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final String messageKey;

    public ConflictException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }
}
