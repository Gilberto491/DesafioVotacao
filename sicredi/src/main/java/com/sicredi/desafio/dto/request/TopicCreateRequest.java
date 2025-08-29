package com.sicredi.desafio.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TopicCreateRequest(
        @NotBlank String title,
        String description
) {}
