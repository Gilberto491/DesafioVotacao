package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.domain.enumerations.VotingSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {

    boolean existsByTopicIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
            Long topicId, VotingSessionStatus status, LocalDateTime now1, LocalDateTime now2);

    boolean existsByIdAndStatusAndOpensAtBeforeAndClosesAtAfter(
            Long topicId, VotingSessionStatus status, LocalDateTime now1, LocalDateTime now2);

}
