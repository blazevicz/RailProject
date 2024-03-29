CREATE TABLE train_stats
(
    train_stats_id   SERIAL PRIMARY KEY,
    line_number      INTEGER,
    line_id          INT,
    train_analyse_id INT,
    first_kilometer  DOUBLE PRECISION,
    last_kilometer   DOUBLE PRECISION,
    train_kwr        INTEGER,
    train_id         INT,
    analyse_train_analyse_id INT,
    train_entity_train_id    INT,
    station          VARCHAR(64),


    CONSTRAINT fk_train_stats_line
        FOREIGN KEY (line_id)
            REFERENCES line (line_id)

);
