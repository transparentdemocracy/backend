package be.tr.democracy.query;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

import java.util.List;
import java.util.Optional;

import static be.tr.democracy.query.ObjectMother.*;

public enum DummyMotionsReadModel implements MotionsReadModel {
    INSTANCE;

    private static final List<Motion> MOTIONS = List.of(
            ObjectMother.FOURTH_MOTION,
            FIRST_MOTION,
            THIRD_MOTION,
            SECOND_MOTION
    );

    static final MotionGroup DUMMY_GROUP_1 = new MotionGroup("groupId1",
            "grouptitleNL1",
            "grouptitleFR1",
            List.of(FIRST_MOTION, SECOND_MOTION), "2024-05-25"
    );
    static final MotionGroup DUMMY_GROUP_2 = new MotionGroup("groupId2",
            "grouptitleNL2",
            "grouptitleFR2",
            List.of(THIRD_MOTION, FOURTH_MOTION), "2024-05-25"
    );
    static final List<MotionGroup> DUMMY_MOTION_GROUPS = List.of(DUMMY_GROUP_1, DUMMY_GROUP_2);



    @Override
    public Page<MotionGroup> find(String searchTerm, PageRequest pageRequest) {
        return Page.slicePageFromList(pageRequest, DUMMY_MOTION_GROUPS);
    }

    @Override
    public Optional<MotionGroup> getMotion(String motionId) {

        return DUMMY_MOTION_GROUPS.stream()
                .filter(x -> x.containsMotion(motionId))
                .findFirst();
    }
}
