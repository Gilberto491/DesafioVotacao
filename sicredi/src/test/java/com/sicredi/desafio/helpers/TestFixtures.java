package com.sicredi.desafio.helpers;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.Vote;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@UtilityClass
public class TestFixtures {
    public Topic topic(String title, String description) {
        return Topic
                .builder()
                .title(title)
                .description(description)
                .status(TopicStatus.PENDING)
                .build();
    }

    public Vote vote(Topic topic, String associate, VoteChoice choice) {
        return Vote.builder()
                .topic(topic)
                .associateId(associate)
                .choice(choice)
                .build();
    }

    public VotingSession session(Topic topic, VotingSessionStatus status,
                                 Instant opensAt, Instant closesAt) {
        return VotingSession.builder()
                .topic(topic)
                .status(status)
                .opensAt(LocalDateTime.ofInstant(opensAt, ZoneOffset.UTC))
                .closesAt(LocalDateTime.ofInstant(closesAt, ZoneOffset.UTC))
                .build();
    }
}
