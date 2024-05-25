package be.tr.democracy.query;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

import java.util.Optional;

public interface MotionsReadModel {

    Page<MotionGroup> find(String searchTerm, PageRequest pageRequest);

    Optional<MotionGroup> getMotion(String motionId);
}
