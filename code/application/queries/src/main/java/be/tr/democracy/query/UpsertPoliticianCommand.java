package be.tr.democracy.query;

import be.tr.democracy.api.UpsertPlenary;
import be.tr.democracy.api.UpsertPolitician;
import be.tr.democracy.vocabulary.plenary.Plenary;
import be.tr.democracy.vocabulary.plenary.Politician;

public class UpsertPoliticianCommand implements UpsertPolitician {

    private final PoliticianWriteModel politicianWriteModel;

    public UpsertPoliticianCommand(PoliticianWriteModel politicianWriteModel) {
        this.politicianWriteModel = politicianWriteModel;
    }

    @Override
    public void upsert(Politician politician) {
        politicianWriteModel.upsert(politician);
    }
}
