package com.sicredi.desafio.dto.request;

import com.sicredi.desafio.service.enumerations.VoteChoice;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VoteCreateRequest(
        @NotNull VoteChoice choice,
        @NotNull
        @Pattern(regexp = "\\d{11}", message = "CPF must have 11 digits")
        String cpf
) {
}
