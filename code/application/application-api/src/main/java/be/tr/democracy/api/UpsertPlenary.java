package be.tr.democracy.api;

import be.tr.democracy.vocabulary.plenary.Plenary;

public interface UpsertPlenary {

    // TODO CATALYST should have its own request model?
    void upsert(Plenary plenary);
}
