package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.VoteCountResponse;
import com.sicredi.desafio.dto.response.VoteResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.exception.UnableToVoteException;
import com.sicredi.desafio.exception.UnprocessableEntityException;
import com.sicredi.desafio.external.dto.VoterStatusResponse;
import com.sicredi.desafio.external.enumerations.VoterStatus;
import com.sicredi.desafio.external.service.VoterRegistryClient;
import com.sicredi.desafio.helpers.SystemTimeProvider;
import com.sicredi.desafio.mapper.VoteMapper;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.repository.VoteRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.VotingService;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import com.sicredi.desafio.service.enumerations.VoteResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VotingServiceImpl implements VotingService {

    private final TopicRepository topicRepo;
    private final VotingSessionRepository sessionRepo;
    private final VoteRepository voteRepo;
    private final SystemTimeProvider timeProvider;
    private final VoteMapper mapper;
    private final VoterRegistryClient voterRegistryClient;


    @Override
    public boolean canOpenSession(Long topicId, LocalDateTime now) {
        log.info("Checking if can open session topicId={} now={}", topicId, now);
        assertTopicExists(topicId);
        return !sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                topicId, VotingSessionStatus.OPEN, now, now);
    }

    @Override
    public boolean isSessionOpenNow(Long sessionId, LocalDateTime now) {
        log.info("Checking if session is open now sessionId={} now={}", sessionId, now);
        var session = getSessionOr404(sessionId);
        return session.isOpenAt(now);
    }

    @Override
    @Transactional
    public VoteResponse vote(Long sessionId, VoteCreateRequest req) {
        log.info("Registering vote sessionId={} choice={}",
                sessionId, req.choice());
        var session = getSessionOr404(sessionId);

        if (!session.isOpenAt(timeProvider.nowUtc()))
            throw new UnprocessableEntityException("session.not-open");

        Long topicId = session.getTopic().getId();

        if (voteRepo.existsByTopic_IdAndAssociateId(topicId, req.cpf()))
            throw new ConflictException("voting.associate-already-voted");

        VoterStatusResponse voter = voterRegistryClient.checkCpf(req.cpf());
        if (voter.status() == VoterStatus.UNABLE_TO_VOTE) {
            throw new UnableToVoteException();
        }

        return mapper.toResponse(
                voteRepo.save(
                        mapper.toEntity(
                                req,
                                session,
                                timeProvider.nowUtc())),
                sessionId
        );
    }

    @Override
    public VoteCountResponse count(Long sessionId) {
        log.info("Counting votes sessionId={}", sessionId);
        var session = getSessionOr404(sessionId);

        if (session.isOpenAt(timeProvider.nowUtc()))
            throw new UnprocessableEntityException("voting.session-open");

        Long topicId = session.getTopic().getId();
        long yes = voteRepo.countByTopic_IdAndChoice(topicId, VoteChoice.SIM);
        long no = voteRepo.countByTopic_IdAndChoice(topicId, VoteChoice.NAO);

        var result = (yes == no) ? VoteResult.TIED : (yes > no ? VoteResult.APPROVED : VoteResult.REJECTED);
        return new VoteCountResponse(sessionId, topicId, yes, no, result);
    }

    private VotingSession getSessionOr404(Long sessionId) {
        return sessionRepo.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("session.not-found"));
    }

    private void assertTopicExists(Long topicId) {
        if (!topicRepo.existsById(topicId))
            throw new NotFoundException("topic.not-found");
    }
}
