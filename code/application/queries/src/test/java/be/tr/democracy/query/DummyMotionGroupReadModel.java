package be.tr.democracy.query;

import static be.tr.democracy.query.ObjectMother.FIRST_MOTION;
import static be.tr.democracy.query.ObjectMother.FOURTH_MOTION;
import static be.tr.democracy.query.ObjectMother.SECOND_MOTION;
import static be.tr.democracy.query.ObjectMother.THIRD_MOTION;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum DummyMotionGroupReadModel implements MotionGroupReadModel {
    INSTANCE;

    private static final List<Motion> MOTIONS = List.of(
        ObjectMother.FOURTH_MOTION,
        FIRST_MOTION,
        THIRD_MOTION,
        SECOND_MOTION
    );

    static final MotionGroup DUMMY_GROUP_1 = new MotionGroup(
        "groupId1",
        "123",
        "1",
        "grouptitleNL1",
        "grouptitleFR1",
        null,
        List.of(FIRST_MOTION, SECOND_MOTION),
        "2024-05-25"
    );
    static final MotionGroup DUMMY_GROUP_2 = new MotionGroup(
        "groupId2",
        "123",
        "2",
        "grouptitleNL2",
        "grouptitleFR2",
        null,
        List.of(THIRD_MOTION, FOURTH_MOTION),
        "2024-05-25"
    );
    static final List<MotionGroup> DUMMY_MOTION_GROUPS = List.of(DUMMY_GROUP_1, DUMMY_GROUP_2);

    @Override
    public Page<MotionGroup> find(String searchTerm, PageRequest pageRequest) {
        return Page.slicePageFromList(pageRequest, DUMMY_MOTION_GROUPS);
    }

    @Override
    @Nullable
    public MotionGroup getMotionGroup(String motionId) {
        return DUMMY_MOTION_GROUPS.stream()
            .filter(x -> x.containsMotion(motionId))
            .findFirst()
            .orElse(null);
    }
}
