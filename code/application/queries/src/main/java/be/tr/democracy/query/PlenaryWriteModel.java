package be.tr.democracy.query;

import be.tr.democracy.vocabulary.plenary.Plenary;

public interface PlenaryWriteModel {

    void upsert(Plenary plenary);

}
