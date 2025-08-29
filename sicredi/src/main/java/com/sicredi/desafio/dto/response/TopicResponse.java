package com.sicredi.desafio.dto.response;

import com.sicredi.desafio.domain.enumerations.TopicStatus;

import java.time.LocalDateTime;

public record TopicResponse(
        Long id,
        String title,
        String description,
        TopicStatus status,
        LocalDateTime createdAt
) {
}
