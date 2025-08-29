package com.sicredi.desafio.dto.response;

import java.time.LocalDateTime;

public record TopicResponse(
        Long id,
        String title,
        String description,
        boolean active,
        LocalDateTime createdAt
) {
}
