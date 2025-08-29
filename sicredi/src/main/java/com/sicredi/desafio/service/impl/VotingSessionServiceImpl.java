package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.VotingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class VotingSessionServiceImpl implements VotingSessionService {

    private static final int DEFAULT_DURATION_MIN = 1;
    private final VotingSessionRepository sessionRepo;
    private final TopicRepository topicRepo;

    @Override
    @Transactional
    public SessionResponse openSession(Long topicId, SessionCreateRequest req) {
        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found"));

        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);

        boolean alreadyOpen = sessionRepo
                .existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                        topicId, VotingSessionStatus.OPEN, nowUtc, nowUtc);
        if (alreadyOpen) {
            throw new ConflictException("There is already an open session for this topic");
        }

        int minutes = (req != null && req.durationMinutes() != null && req.durationMinutes() > 0)
                ? req.durationMinutes()
                : DEFAULT_DURATION_MIN;

        var s = VotingSession.builder()
                .topic(topic)
                .opensAt(nowUtc)
                .closesAt(nowUtc.plusMinutes(minutes))
                .durationMinutes(minutes)
                .status(VotingSessionStatus.OPEN)
                .build();

        var saved = sessionRepo.save(s);

        return new SessionResponse(
                saved.getId(),
                topic.getId(),
                saved.getOpensAt().toInstant(ZoneOffset.UTC),
                saved.getClosesAt().toInstant(ZoneOffset.UTC)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SessionResponse getById(Long sessionId) {
        VotingSession s = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));
        return new SessionResponse(
                s.getId(),
                s.getTopic().getId(),
                s.getOpensAt().toInstant(ZoneOffset.UTC),
                s.getClosesAt().toInstant(ZoneOffset.UTC)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canOpenSession(Long topicId, LocalDateTime nowUtc) {
        if (!topicRepo.existsById(topicId)) {
            throw new NotFoundException("Topic not found");
        }
        return !sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                topicId, VotingSessionStatus.OPEN, nowUtc, nowUtc);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionOpenNow(Long sessionId, LocalDateTime nowUtc) {
        if (!sessionRepo.existsById(sessionId)) {
            throw new NotFoundException("Session not found");
        }
        return sessionRepo.existsByIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                sessionId, VotingSessionStatus.OPEN, nowUtc, nowUtc);
    }
}
