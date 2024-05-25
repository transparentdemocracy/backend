package be.tr.democracy.api;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

import java.util.Optional;

public interface MotionsService {

    Page<Motion> findMotions(String searchTerm, PageRequest pageRequest);

    Optional<Motion> getMotion(String motionId);
}
