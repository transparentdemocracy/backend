package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.List;
import java.util.Optional;

public enum DummyMotionsService implements MotionsService {
    INSTANCE;


    @Override
    public Page<Motion> findMotions(String searchTerm, PageRequest pageRequest) {
        return new Page<Motion>(1, 1, 1, MotionsMother.DUMMY_MOTIONS);
    }

    @Override
    public Optional<Motion> getMotion(String motionId) {
        return MotionsMother.DUMMY_MOTIONS.stream().filter(x -> x.motionId().equalsIgnoreCase(motionId))
                .toList().stream().findFirst();
    }
}
