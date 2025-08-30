package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.helpers.TestFixtures;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class VoteRepositoryTest {

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    TopicRepository topicRepository;
    Topic topic;

    @BeforeEach
    void setup() {
        topic = topicRepository.save(TestFixtures.topic("Pauta X", "Description Pauta X"));
    }

    @Test
    void existsByTopicAndAssociateId_returnsTrueWhenDuplicate() {
        voteRepository.save(TestFixtures.vote(topic, "123", VoteChoice.SIM));

        assertThat(voteRepository.existsByTopic_IdAndAssociateId(topic.getId(), "123")).isTrue();
        assertThat(voteRepository.existsByTopic_IdAndAssociateId(topic.getId(), "999")).isFalse();
    }

    @Test
    void countByTopicAndChoice_countsCorrectly() {
        voteRepository.saveAll(List.of(
                TestFixtures.vote(topic, "a1", VoteChoice.SIM),
                TestFixtures.vote(topic, "a2", VoteChoice.SIM),
                TestFixtures.vote(topic, "a3", VoteChoice.NAO)
        ));

        assertThat(voteRepository.countByTopic_IdAndChoice(topic.getId(), VoteChoice.SIM)).isEqualTo(2);
        assertThat(voteRepository.countByTopic_IdAndChoice(topic.getId(), VoteChoice.NAO)).isEqualTo(1);
    }
}
