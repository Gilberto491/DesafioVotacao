CREATE TABLE topic (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    status       VARCHAR(20)  NOT NULL DEFAULT 'CREATED',
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voting_session (
    id               BIGSERIAL PRIMARY KEY,
    topic_id         BIGINT      NOT NULL,
    opens_at         TIMESTAMP   NOT NULL,
    closes_at        TIMESTAMP   NOT NULL,
    duration_minutes INTEGER     NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_session_topic     FOREIGN KEY (topic_id) REFERENCES topic (id),
    CONSTRAINT uk_session_per_topic UNIQUE (topic_id)
);

CREATE TABLE vote (
    id            BIGSERIAL PRIMARY KEY,
    topic_id      BIGINT      NOT NULL,
    associate_id  VARCHAR(11) NOT NULL,
    choice        BOOLEAN     NOT NULL,
    voted_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vote_topic          FOREIGN KEY (topic_id) REFERENCES topic (id),
    CONSTRAINT uk_one_vote_per_topic  UNIQUE (topic_id, associate_id)
);

CREATE INDEX idx_vote_topic_choice ON vote (topic_id, choice);

CREATE INDEX idx_voting_session_open ON voting_session (topic_id) WHERE status = 'OPEN';
