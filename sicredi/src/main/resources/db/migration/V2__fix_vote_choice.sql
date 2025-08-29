ALTER TABLE vote
ALTER COLUMN choice TYPE varchar(10)
  USING CASE
         WHEN choice IS TRUE  THEN 'SIM'
         WHEN choice IS FALSE THEN 'NAO'
         ELSE NULL
END;

ALTER TABLE voting_session DROP CONSTRAINT IF EXISTS uk_session_per_topic;
CREATE UNIQUE INDEX IF NOT EXISTS uk_voting_session_open_per_topic
    ON voting_session(topic_id) WHERE status = 'OPEN';