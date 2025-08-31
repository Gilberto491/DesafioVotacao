package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.helpers.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static com.sicredi.desafio.helpers.TestConstants.DESCRIPTION_TOPIC;
import static com.sicredi.desafio.helpers.TestConstants.NAME_TOPIC;
import static com.sicredi.desafio.helpers.TestConstants.NAME_TOPIC_2;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    private Topic topic;

    @BeforeEach
    void setUp() {
        topic = topicRepository.save(
                TestFixtures.topic(NAME_TOPIC, DESCRIPTION_TOPIC)
        );
    }

    @Test
    void existsByTitleIgnoreCase_returnsTrueWhenDuplicate() {
        assertThat(topicRepository.existsByTitleIgnoreCase(NAME_TOPIC)).isTrue();
        assertThat(topicRepository.existsByTitleIgnoreCase(NAME_TOPIC_2)).isFalse();
    }

    @Test
    void saveAndFindById_persistsCorrectly() {
        var found = topicRepository.findById(topic.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo(NAME_TOPIC);
        assertThat(found.getDescription()).isEqualTo(DESCRIPTION_TOPIC);
        assertThat(found.getStatus()).isEqualTo(TopicStatus.PENDING);
    }

    @Test
    void deleteById_removesEntity() {
        topicRepository.deleteById(topic.getId());
        assertThat(topicRepository.findById(topic.getId())).isEmpty();
    }

    @Test
    void existsByIdAndStatus_returnsTrue_whenTopicHasStatus() {
        topic.setStatus(TopicStatus.USED);
        topicRepository.save(topic);

        boolean exists = topicRepository.existsByIdAndStatus(topic.getId(), TopicStatus.USED);
        assertThat(exists).isTrue();
    }
}
