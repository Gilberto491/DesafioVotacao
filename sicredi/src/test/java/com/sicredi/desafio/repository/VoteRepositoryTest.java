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

import static com.sicredi.desafio.helpers.TestConstants.CPF;
import static com.sicredi.desafio.helpers.TestConstants.CPF_2;
import static com.sicredi.desafio.helpers.TestConstants.CPF_3;
import static com.sicredi.desafio.helpers.TestConstants.DESCRIPTION_TOPIC;
import static com.sicredi.desafio.helpers.TestConstants.NAME_TOPIC;
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
        topic = topicRepository.save(TestFixtures.topic(NAME_TOPIC, DESCRIPTION_TOPIC));
    }

    @Test
    void existsByTopicAndAssociateId_returnsTrueWhenDuplicate() {
        voteRepository.save(TestFixtures.vote(topic, CPF, VoteChoice.SIM));

        assertThat(voteRepository.existsByTopic_IdAndAssociateId(topic.getId(), CPF)).isTrue();
        assertThat(voteRepository.existsByTopic_IdAndAssociateId(topic.getId(), CPF_2)).isFalse();
    }

    @Test
    void countByTopicAndChoice_countsCorrectly() {
        voteRepository.saveAll(List.of(
                TestFixtures.vote(topic, CPF, VoteChoice.SIM),
                TestFixtures.vote(topic, CPF_2, VoteChoice.SIM),
                TestFixtures.vote(topic, CPF_3, VoteChoice.NAO)
        ));

        assertThat(voteRepository.countByTopic_IdAndChoice(topic.getId(), VoteChoice.SIM)).isEqualTo(2);
        assertThat(voteRepository.countByTopic_IdAndChoice(topic.getId(), VoteChoice.NAO)).isEqualTo(1);
    }

    @Test
    void deleteByTopic_Id_shouldRemoveVotesByTopic() {
        voteRepository.save(TestFixtures.vote(topic, CPF, VoteChoice.SIM));
        voteRepository.save(TestFixtures.vote(topic, CPF_2, VoteChoice.NAO));

        assertThat(voteRepository.count()).isEqualTo(2);
        voteRepository.deleteByTopic_Id(topic.getId());
        assertThat(voteRepository.count()).isEqualTo(0);
    }
}
