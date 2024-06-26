package be.tr.democracy.rest;

import be.tr.democracy.api.FindMotions;
import be.tr.democracy.api.GetMotionGroup;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

import java.util.Optional;

public enum DummyFindMotions implements FindMotions, GetMotionGroup {
    INSTANCE;

    @Override
    public Page<MotionGroup> findMotions(String searchTerm, PageRequest pageRequest) {
        return new Page<>(1, 1, 1, MotionsMother.DUMMY_MOTION_GROUPS);
    }

    @Override
    public Optional<MotionGroup> getMotionGroup(String motionId) {
        return MotionsMother.DUMMY_MOTION_GROUPS.stream()
                .filter(group -> group.containsMotion(motionId))
                .findFirst();
    }

}

