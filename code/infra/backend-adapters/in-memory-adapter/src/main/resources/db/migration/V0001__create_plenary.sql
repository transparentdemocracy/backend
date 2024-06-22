CREATE TABLE plenary (
    plenary_id VARCHAR NOT NULL PRIMARY KEY,
    legislature VARCHAR NOT NULL,
    data JSONB NOT NULL,
    content TEXT
);