package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.Vote;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByTopic_IdAndAssociateId(Long topicId, String associateId);

    long countByTopic_IdAndChoice(Long topicId, VoteChoice choice);
}
