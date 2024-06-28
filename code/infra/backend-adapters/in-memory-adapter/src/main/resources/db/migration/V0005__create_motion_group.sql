CREATE TABLE motion_group_link
(
    plenary_id  VARCHAR NOT NULL,
    id          VARCHAR NOT NULL,
    legislature VARCHAR NOT NULL,
    data        JSONB   NOT NULL,
    content     TEXT,
    PRIMARY KEY (plenary_id, id)
);

-- motion_group contains same information as motion_group_link but is enriched with votes -> should we use the same model?
-- do we even need motion_group_link?
CREATE TABLE motion_group
(
    -- primary key should be composed of plenary_id and id
    plenary_id VARCHAR NOT NULL,
    id         VARCHAR NOT NULL,
    data       JSONB   NOT NULL,
    content    TEXT,
    PRIMARY KEY (plenary_id, id)
);