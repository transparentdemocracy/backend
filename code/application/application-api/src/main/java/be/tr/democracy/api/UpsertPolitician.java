package be.tr.democracy.api;

import be.tr.democracy.vocabulary.plenary.Politician;

public interface UpsertPolitician {

    void upsert(Politician politician);
}
