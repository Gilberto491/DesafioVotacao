package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.Vote;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.VoteCountResponse;
import com.sicredi.desafio.dto.response.VoteResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.exception.UnprocessableEntityException;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.repository.VoteRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.VotingService;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import com.sicredi.desafio.service.enumerations.VoteResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class VotingServiceImpl implements VotingService {

    private final TopicRepository topicRepo;
    private final VotingSessionRepository sessionRepo;
    private final VoteRepository voteRepo;

    @Override
    @Transactional(readOnly = true)
    public boolean canOpenSession(Long topicId, LocalDateTime now) {
        if (!topicRepo.existsById(topicId)) return false;
        return !sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(topicId, VotingSessionStatus.OPEN, now, now);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionOpenNow(Long sessionId, LocalDateTime now) {
        var s = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));
        return !now.isBefore(s.getOpensAt()) && !now.isAfter(s.getClosesAt());
    }

    @Override
    @Transactional
    public VoteResponse vote(Long sessionId, VoteCreateRequest req) {
        var session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        Long topicId = session.getTopic().getId();

        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        boolean open = sessionRepo.existsByIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                sessionId, VotingSessionStatus.OPEN, nowUtc, nowUtc);
        if (!open) {
            throw new UnprocessableEntityException("Session is not open");
        }

        if (voteRepo.existsByTopic_IdAndAssociateId(topicId, req.associateId())) {
            throw new ConflictException("Associate has already voted on this topic");
        }

        var entity = Vote.builder()
                .topic(session.getTopic())
                .associateId(req.associateId())
                .choice(req.choice())
                .votedAt(nowUtc)
                .build();

        Vote saved = voteRepo.save(entity);

        return new VoteResponse(
                saved.getId(),
                sessionId,
                saved.getAssociateId(),
                saved.getChoice(),
                saved.getVotedAt()
        );

    }

    @Override
    @Transactional(readOnly = true)
    public VoteCountResponse count(Long sessionId) {
        var nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        var s = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        boolean open = sessionRepo.existsByIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                sessionId, VotingSessionStatus.OPEN, nowUtc, nowUtc);
        if (open) {
            throw new UnprocessableEntityException("Voting session is still open");
        }

        Long topicId = s.getTopic().getId();
        long yes = voteRepo.countByTopic_IdAndChoice(topicId, VoteChoice.SIM);
        long no = voteRepo.countByTopic_IdAndChoice(topicId, VoteChoice.NAO);

        var result = yes == no ? VoteResult.TIED : (yes > no ? VoteResult.APPROVED : VoteResult.REJECTED);

        return new VoteCountResponse(sessionId, topicId, yes, no, result);
    }
}
