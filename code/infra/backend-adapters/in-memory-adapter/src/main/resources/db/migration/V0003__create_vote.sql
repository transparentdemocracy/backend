CREATE TABLE vote
(
    voting_id     VARCHAR NOT NULL,
    politician_id VARCHAR NOT NULL,
    vote_type     VARCHAR,
    PRIMARY KEY (voting_id, politician_id)
);