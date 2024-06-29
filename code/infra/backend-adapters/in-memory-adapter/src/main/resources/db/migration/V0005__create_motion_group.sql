CREATE TABLE motion_group
(
    plenary_id VARCHAR NOT NULL,
    id         VARCHAR NOT NULL,
    data       JSONB   NOT NULL,
    content    TEXT,
    PRIMARY KEY (plenary_id, id)
);