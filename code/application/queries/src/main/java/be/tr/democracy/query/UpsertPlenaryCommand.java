package be.tr.democracy.query;

import be.tr.democracy.api.UpsertPlenary;
import be.tr.democracy.vocabulary.plenary.Plenary;

public class UpsertPlenaryCommand implements UpsertPlenary {

    private final PlenaryWriteModel plenaryWriteModel;

    public UpsertPlenaryCommand(PlenaryWriteModel plenaryWriteModel) {
        this.plenaryWriteModel = plenaryWriteModel;
    }

    @Override
    public void upsert(Plenary plenary) {
        plenaryWriteModel.upsert(plenary);
    }
}
