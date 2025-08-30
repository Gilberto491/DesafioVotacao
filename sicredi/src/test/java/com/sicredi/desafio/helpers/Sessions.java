package com.sicredi.desafio.helpers;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class Sessions {

    public VotingSession openSessionAround(Topic topic, Instant now, long closesInMinutes) {
        return TestFixtures.session(
                topic,
                VotingSessionStatus.OPEN,
                now.minus(1, ChronoUnit.MINUTES),
                now.plus(closesInMinutes, ChronoUnit.MINUTES)
        );
    }

    public VotingSession openSessionNowWithId(long id, Topic topic, LocalDateTime now) {
        return VotingSession.builder()
                .id(id)
                .topic(topic)
                .status(VotingSessionStatus.OPEN)
                .opensAt(now.minusMinutes(1))
                .closesAt(now.plusMinutes(5))
                .build();
    }
}
