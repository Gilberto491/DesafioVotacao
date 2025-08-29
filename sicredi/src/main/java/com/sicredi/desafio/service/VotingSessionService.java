package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;

public interface VotingSessionService {

    SessionResponse openSession(Long topicId, SessionCreateRequest req);

}
