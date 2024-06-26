CREATE TABLE motion_group_link
(
    id      VARCHAR NOT NULL PRIMARY KEY,
    data    JSONB   NOT NULL,
    content TEXT
);

-- motion_group contains same information as motion_group_link but is enriched with votes -> should we use the same model?
-- do we even need motion_group_link?
CREATE TABLE motion_group
(
    id         VARCHAR NOT NULL PRIMARY KEY,
    plenary_id VARCHAR NOT NULL,
    data       JSONB   NOT NULL,
    content    TEXT
);