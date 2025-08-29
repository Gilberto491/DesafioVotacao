package com.sicredi.desafio.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String messageKey;

    public NotFoundException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }
}
