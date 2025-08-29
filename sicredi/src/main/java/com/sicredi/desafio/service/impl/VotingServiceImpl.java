package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.repository.VoteRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.VotingService;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotingServiceImpl implements VotingService {

    private final TopicRepository topicRepo;
    private final VotingSessionRepository sessionRepo;
    private final VoteRepository voteRepo;

    @Transactional(readOnly = true)
    public boolean canOpenSession(Long topicId, LocalDateTime now) {
        if (!topicRepo.existsById(topicId)) return false;
        return !sessionRepo.existsByTopic_IdAndOpensAtLessThanEqualAndClosesAtGreaterThan(topicId, now, now);
    }

    @Transactional(readOnly = true)
    public boolean isSessionOpenNow(Long sessionId, LocalDateTime now) {
        var s = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));
        return !now.isBefore(s.getOpensAt()) && !now.isAfter(s.getClosesAt());
    }

    @Transactional
    public void castVote(Long sessionId, String voterId, VoteChoice choice) {
        if (voteRepo.existsByTopic_IdAndAssociateId(sessionId, voterId)) {
            throw new ConflictException("Voter has already voted in this session");
        }
    }
}
