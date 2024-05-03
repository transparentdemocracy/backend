package be.tr.democracy.query;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;

import java.util.List;

import static java.util.Objects.requireNonNull;


public class MotionsQuery implements MotionsService {

    private final MotionsReadModel motionsReadModel;

    public MotionsQuery(MotionsReadModel motionsReadModel) {
        requireNonNull(motionsReadModel);
        this.motionsReadModel = motionsReadModel;
    }

    @Override
    public List<Motion> getMotions() {
        return motionsReadModel.loadAll();
    }
}
