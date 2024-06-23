package be.tr.democracy.query;

import be.tr.democracy.vocabulary.plenary.Politician;

public interface PoliticianWriteModel {

    void upsert(Politician politician);

}
