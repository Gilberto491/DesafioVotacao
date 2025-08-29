package com.sicredi.desafio.external.exception;

public class CpfNotFoundException extends RuntimeException {
    public CpfNotFoundException(String cpf) {
        super("CPF not found: " + cpf);
    }
}
