package com.sicredi.desafio.service;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.Vote;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.VoteResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.UnableToVoteException;
import com.sicredi.desafio.exception.UnprocessableEntityException;
import com.sicredi.desafio.external.dto.VoterStatusResponse;
import com.sicredi.desafio.external.enumerations.VoterStatus;
import com.sicredi.desafio.external.service.VoterRegistryClient;
import com.sicredi.desafio.helpers.Sessions;
import com.sicredi.desafio.helpers.SystemTimeProvider;
import com.sicredi.desafio.mapper.VoteMapper;
import com.sicredi.desafio.repository.VoteRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import com.sicredi.desafio.service.impl.VotingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotingServiceImplTest {

    @Mock
    private VoteRepository voteRepo;
    @Mock
    private VotingSessionRepository sessionRepo;
    @Mock
    private VoterRegistryClient voterRegistryClient;
    @Mock
    private VoteMapper mapper;
    @Mock
    private SystemTimeProvider timeProvider;

    @InjectMocks
    private VotingServiceImpl service;

    private VotingSession session;
    private Topic topic;
    private LocalDateTime now;

    @BeforeEach
    void init() {
        now = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        topic = Topic.builder().id(1L).title("Pauta 1").build();
        session = Sessions.openSessionNowWithId(10L, topic, now);
        when(timeProvider.nowUtc()).thenReturn(now);
    }

    @Test
    void vote_throwsConflict_whenAssociateAlreadyVoted() {
        when(timeProvider.nowUtc()).thenReturn(now);

        when(sessionRepo.findById(10L)).thenReturn(Optional.of(session));
        when(voteRepo.existsByTopic_IdAndAssociateId(1L, "12345678910")).thenReturn(true);

        var req = new VoteCreateRequest(VoteChoice.SIM, "12345678910");

        assertThatThrownBy(() -> service.vote(10L, req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("voting.associate-already-voted");
    }

    @Test
    void vote_persists_whenOk() {
        var session = Sessions.openSessionNowWithId(10L, topic, now);

        when(sessionRepo.findById(10L)).thenReturn(Optional.of(session));
        when(voteRepo.existsByTopic_IdAndAssociateId(1L, "11122233344")).thenReturn(false);
        when(voterRegistryClient.checkCpf("11122233344"))
                .thenReturn(new VoterStatusResponse(VoterStatus.ABLE_TO_VOTE));

        var req = new VoteCreateRequest(VoteChoice.SIM, "11122233344");

        var entity = Vote.builder()
                .id(99L).topic(topic)
                .associateId("11122233344").choice(VoteChoice.SIM).votedAt(now)
                .build();

        var resp = new VoteResponse(99L, 10L, "11122233344", VoteChoice.SIM, now);

        when(mapper.toEntity(req, session, now)).thenReturn(entity);
        when(voteRepo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity, 10L)).thenReturn(resp);

        assertThat(service.vote(10L, req)).isEqualTo(resp);
    }

    @Test
    void vote_unable_whenRegistrySaysUnable() {
        when(sessionRepo.findById(10L)).thenReturn(Optional.of(session));
        when(voteRepo.existsByTopic_IdAndAssociateId(1L, "99988877766")).thenReturn(false);
        when(voterRegistryClient.checkCpf("99988877766"))
                .thenReturn(new VoterStatusResponse(VoterStatus.UNABLE_TO_VOTE));

        var req = new VoteCreateRequest(VoteChoice.NAO, "99988877766");

        assertThatThrownBy(() -> service.vote(10L, req))
                .isInstanceOf(UnableToVoteException.class);
    }

    @Test
    void count_unprocessable_whenOpen() {
        when(sessionRepo.findById(10L)).thenReturn(Optional.of(session));
        assertThatThrownBy(() -> service.count(10L))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessageContaining("session-open");
    }
}
