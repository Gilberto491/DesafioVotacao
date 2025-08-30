package com.sicredi.desafio.service;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.helpers.ConvertDate;
import com.sicredi.desafio.helpers.SystemTimeProvider;
import com.sicredi.desafio.helpers.TestFixtures;
import com.sicredi.desafio.mapper.SessionMapper;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.repository.VotingSessionRepository;
import com.sicredi.desafio.service.impl.VotingSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotingSessionServiceImplTest {

    @Mock
    private VotingSessionRepository sessionRepo;
    @Mock
    private TopicRepository topicRepo;
    @Mock
    private SessionMapper mapper;
    @Mock
    private SystemTimeProvider timeProvider;

    @InjectMocks
    private VotingSessionServiceImpl service;

    private Topic topic;
    private VotingSession session;
    private SessionResponse response;
    private LocalDateTime now;

    @BeforeEach
    void init() {
        Instant now = ConvertDate.instantNow();
        topic = Topic.builder().id(1L).title("Sessão Pauta").build();
        session = TestFixtures.session(topic, VotingSessionStatus.OPEN, now, ConvertDate.instantPlusMinutes(1));
        response = new SessionResponse(10L, topic.getId(), now, ConvertDate.instantPlusMinutes(1));
    }

    @Test
    void openSession_createsNewSession_whenNoConflict() {
        when(timeProvider.nowUtc()).thenReturn(now);
        when(sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(topic.getId(), VotingSessionStatus.OPEN, now, now))
                .thenReturn(false);
        when(topicRepo.findById(topic.getId())).thenReturn(Optional.of(topic));
        when(sessionRepo.save(any())).thenReturn(session);
        when(mapper.toEntity(eq(topic), any(), anyInt())).thenReturn(session);
        when(mapper.toResponse(session)).thenReturn(response);

        SessionResponse out = service.openSession(topic.getId(), new SessionCreateRequest(1));

        assertThat(out).isEqualTo(response);
    }

    @Test
    void openSession_throwsConflict_whenAlreadyOpenSessionExists() {
        when(timeProvider.nowUtc()).thenReturn(now);
        when(sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(topic.getId(), VotingSessionStatus.OPEN, now, now))
                .thenReturn(true);

        assertThatThrownBy(() -> service.openSession(topic.getId(), new SessionCreateRequest(1)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("session.already-open-session");
    }

    @Test
    void openSession_throwsNotFound_whenTopicDoesNotExist() {
        when(timeProvider.nowUtc()).thenReturn(now);
        when(sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(anyLong(), any(), any(), any()))
                .thenReturn(false);
        when(topicRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.openSession(99L, new SessionCreateRequest(1)))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("topic.not-found");
    }
}
