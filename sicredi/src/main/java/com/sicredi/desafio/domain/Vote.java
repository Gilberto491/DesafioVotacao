package com.sicredi.desafio.domain;

import com.sicredi.desafio.service.enumerations.VoteChoice;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "vote",
        indexes = {
                @Index(name = "idx_vote_topic_choice", columnList = "topic_id, choice")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vote_topic_associate", columnNames = {"topic_id", "associate_id"})
        }
)
public class Vote extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_vote_topic"))
    private Topic topic;

    @Column(name = "associate_id", nullable = false, length = 11)
    private String associateId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private VoteChoice choice;

    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt;

    @PrePersist
    void prePersistVote() {
        if (votedAt == null) votedAt = LocalDateTime.from(LocalDateTime.now());
    }

}
