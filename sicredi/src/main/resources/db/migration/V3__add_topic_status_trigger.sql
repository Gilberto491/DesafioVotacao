CREATE OR REPLACE FUNCTION fn_topic_open_on_session_insert()
    RETURNS trigger AS $$
BEGIN
    UPDATE topic
    SET status = 'USED'
    WHERE id = NEW.topic_id
      AND status = 'PENDING';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_topic_open_on_session_insert ON voting_session;

CREATE TRIGGER trg_topic_open_on_session_insert
    AFTER INSERT ON voting_session
    FOR EACH ROW
EXECUTE FUNCTION fn_topic_open_on_session_insert();
