package be.tr.democracy.api;

import be.tr.democracy.vocabulary.motion.SubDocument;

public interface UpsertDocumentSummary {

    void upsert(SubDocument subDocument);
}
