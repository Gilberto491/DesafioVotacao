package com.sicredi.desafio.dto.response;

import java.time.Instant;

public record SessionResponse(
        Long id,
        Long topicId,
        Instant opensAt,
        Instant closesAt
) {
}
