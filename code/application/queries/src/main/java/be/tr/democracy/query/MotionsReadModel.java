package be.tr.democracy.query;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

import java.util.Optional;

public interface MotionsReadModel {

    Page<Motion> find(String searchTerm, PageRequest pageRequest);

    Optional<Motion> getMotion(String motionId);
}
