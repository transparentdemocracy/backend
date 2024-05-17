package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.List;

public enum DummyMotionsService implements MotionsService {
    INSTANCE;

    @Override
    public List<Motion> findMotions(int maximum) {
        return MotionsMother.DUMMY_MOTIONS;
    }

    @Override
    public Page<Motion> findMotions(String searchTerm, PageRequest pageRequest) {
        return new Page<Motion>(1, 1, 1, MotionsMother.DUMMY_MOTIONS);
    }
}
