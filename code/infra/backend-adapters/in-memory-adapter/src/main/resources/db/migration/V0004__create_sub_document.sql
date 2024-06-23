CREATE TABLE sub_document
(
    document_nr INTEGER NOT NULL,
    document_sub_nr INTEGER NOT NULL,
    summary_nl  VARCHAR NOT NULL,
    summary_fr  VARCHAR NOT NULL,
    PRIMARY KEY (document_nr, document_sub_nr)
);