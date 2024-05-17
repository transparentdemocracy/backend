package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.List;

import static be.tr.democracy.query.ObjectMother.*;

public enum DummyMotionsReadModel implements MotionsReadModel {
    INSTANCE;
    private static final List<Motion> MOTIONS = List.of(
            ObjectMother.FOURTH,
            FIRST,
            THIRD,
            SECOND
    );

    @Override
    public List<Motion> loadAll() {
        return MOTIONS;
    }

    @Override
    public Page<Motion> find(String searchTerm, PageRequest pageRequest) {
        return new Page<>(pageRequest.pageNr(), pageRequest.pageSize(), 1, MOTIONS);
    }
}
