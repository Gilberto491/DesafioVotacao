package com.sicredi.desafio.dto.response;

import com.sicredi.desafio.domain.enumerations.TopicStatus;

public record TopicResponse(
        Long id,
        String title,
        String description,
        TopicStatus active
) {}
