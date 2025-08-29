package com.sicredi.desafio.dto.response;

import com.sicredi.desafio.service.enumerations.VoteChoice;

import java.time.LocalDateTime;

public record VoteResponse(
        Long id,
        Long sessionId,
        String associateId,
        VoteChoice choice,
        LocalDateTime votedAt
) {
}
