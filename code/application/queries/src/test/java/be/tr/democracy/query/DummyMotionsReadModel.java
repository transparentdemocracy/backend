package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.List;
import java.util.Optional;

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
    public Page<Motion> find(String searchTerm, PageRequest pageRequest) {
        return Page.slicePageFromList(pageRequest, MOTIONS);
    }

    @Override
    public Optional<Motion> getMotion(String motionId) {
        return MOTIONS.stream().filter(x -> x.motionId().equals(motionId)).findFirst();
    }
}
