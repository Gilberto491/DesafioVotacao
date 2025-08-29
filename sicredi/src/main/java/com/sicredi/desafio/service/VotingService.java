package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.VoteCountResponse;
import com.sicredi.desafio.dto.response.VoteResponse;

import java.time.LocalDateTime;

public interface VotingService {

    boolean canOpenSession(Long topicId, LocalDateTime now);

    boolean isSessionOpenNow(Long sessionId, LocalDateTime now);

    VoteResponse vote(Long sessionId, VoteCreateRequest req);

    VoteCountResponse count(Long sessionId);

}
