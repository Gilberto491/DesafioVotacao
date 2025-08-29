package com.sicredi.desafio.repository;

import com.sicredi.desafio.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByTitleIgnoreCase(String titulo);
}
