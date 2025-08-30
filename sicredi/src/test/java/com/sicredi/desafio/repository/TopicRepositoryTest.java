package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.helpers.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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
                TestFixtures.topic("Pauta X", "Description Pauta X")
        );
    }

    @Test
    void existsByTitleIgnoreCase_returnsTrueWhenDuplicate() {
        assertThat(topicRepository.existsByTitleIgnoreCase("pauta x")).isTrue();
        assertThat(topicRepository.existsByTitleIgnoreCase("outra pauta")).isFalse();
    }

    @Test
    void saveAndFindById_persistsCorrectly() {
        var found = topicRepository.findById(topic.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("Pauta X");
        assertThat(found.getDescription()).isEqualTo("Description Pauta X");
        assertThat(found.getStatus()).isEqualTo(TopicStatus.PENDING);
    }

    @Test
    void deleteById_removesEntity() {
        topicRepository.deleteById(topic.getId());
        assertThat(topicRepository.findById(topic.getId())).isEmpty();
    }
}
