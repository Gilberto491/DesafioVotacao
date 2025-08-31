package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.helpers.ConvertDate;
import com.sicredi.desafio.helpers.Sessions;
import com.sicredi.desafio.helpers.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.sicredi.desafio.helpers.TestConstants.DESCRIPTION_TOPIC;
import static com.sicredi.desafio.helpers.TestConstants.NAME_TOPIC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class VotingSessionRepositoryTest {

    @Autowired
    private VotingSessionRepository sessionRepo;

    @Autowired
    private TopicRepository topicRepo;

    private LocalDateTime now;
    private Topic topic;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        topic = topicRepo.save(
                TestFixtures.topic(NAME_TOPIC, DESCRIPTION_TOPIC)
        );
    }

    @Test
    void existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter_returnsTrueWhenActiveSessionExists() {
        var session = Sessions.openSessionAround(topic, ConvertDate.instantNow(), 5);
        sessionRepo.save(session);

        boolean exists = sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                topic.getId(), VotingSessionStatus.OPEN, now, now
        );

        assertThat(exists).isTrue();
    }

    @Test
    void existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter_returnsFalseWhenNoActiveSession() {
        boolean exists = sessionRepo.existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
                topic.getId(), VotingSessionStatus.OPEN, now, now
        );

        assertThat(exists).isFalse();
    }

    @Test
    void deleteByTopic_Id_shouldRemoveSessionsByTopic() {
        var session1 = Sessions.openSessionAround(topic, ConvertDate.instantNow(), 5);

        sessionRepo.save(session1);

        assertThat(sessionRepo.count()).isEqualTo(1);
        sessionRepo.deleteByTopic_Id(topic.getId());
        assertThat(sessionRepo.count()).isEqualTo(0);
    }
}
