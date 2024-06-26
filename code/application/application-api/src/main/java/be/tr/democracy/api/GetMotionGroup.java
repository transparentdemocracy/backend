package be.tr.democracy.api;

import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

import java.util.Optional;

public interface GetMotionGroup {

    Optional<MotionGroup> getMotionGroup(String motionId);
}
