package com.sicredi.desafio.dto.request;

import com.sicredi.desafio.service.enumerations.VoteChoice;
import jakarta.validation.constraints.NotNull;

public record VoteCreateRequest(
        @NotNull String associateId,
        @NotNull VoteChoice choice
) {}
