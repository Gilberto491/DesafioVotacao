package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.helpers.SystemTimeProvider;
import com.sicredi.desafio.mapper.SessionMapper;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.VotingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VotingSessionServiceImpl implements VotingSessionService {

    private static final int DEFAULT_DURATION_MIN = 1;
    private final VotingSessionRepository sessionRepo;
    private final TopicRepository topicRepo;
    private final SessionMapper mapper;
    private final SystemTimeProvider timeProvider;

    @Override
    @Transactional
    public SessionResponse openSession(Long topicId, SessionCreateRequest req) {
        if (existsOpenSession(topicId, timeProvider.nowUtc())) {
            throw new ConflictException("There is already an open session for this topic");
        }
        return mapper.toResponse
                (sessionRepo.save
                        (mapper.toEntity(
                                getTopicOr404(topicId),
                                timeProvider.nowUtc(),
                                resolveDuration(req))));
    }

    private Topic getTopicOr404(Long topicId) {
        return topicRepo.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found"));
    }

    private int resolveDuration(SessionCreateRequest req) {
        return (req != null && req.durationMinutes() != null && req.durationMinutes() > 0)
                ? req.durationMinutes()
                : DEFAULT_DURATION_MIN;
    }

    private boolean existsOpenSession(Long topicId, LocalDateTime now) {
        return sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                topicId, VotingSessionStatus.OPEN, now, now);
    }
}
