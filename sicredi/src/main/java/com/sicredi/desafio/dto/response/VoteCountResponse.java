package com.sicredi.desafio.dto.response;

import com.sicredi.desafio.service.enumerations.VoteResult;

public record VoteCountResponse(
        Long sessionId,
        Long topicId,
        long yes,
        long no,
        VoteResult result
) {
}
