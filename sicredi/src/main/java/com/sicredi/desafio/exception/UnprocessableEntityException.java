package com.sicredi.desafio.exception;

import lombok.Getter;

@Getter
public class UnprocessableEntityException extends RuntimeException {
    private final String messageKey;

    public UnprocessableEntityException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }
}

