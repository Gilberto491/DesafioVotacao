package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
    boolean existsByTopic_IdAndOpensAtLessThanEqualAndClosesAtGreaterThan(
            Long topicId,
            LocalDateTime now1,
            LocalDateTime now2
    );
}
