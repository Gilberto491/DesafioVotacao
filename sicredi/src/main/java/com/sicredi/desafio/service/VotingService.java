package com.sicredi.desafio.service;

import com.sicredi.desafio.service.enumerations.VoteChoice;

import java.time.LocalDateTime;

public interface VotingService {

    boolean canOpenSession(Long topicId, LocalDateTime now);
    boolean isSessionOpenNow(Long sessionId, LocalDateTime now);
    void castVote(Long sessionId, String voterId, VoteChoice choice);

}
