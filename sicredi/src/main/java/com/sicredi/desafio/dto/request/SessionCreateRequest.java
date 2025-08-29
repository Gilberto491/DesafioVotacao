package com.sicredi.desafio.dto.request;

import jakarta.validation.constraints.Min;

public record SessionCreateRequest(
        @Min(1) Integer durationMinutes
) {}
