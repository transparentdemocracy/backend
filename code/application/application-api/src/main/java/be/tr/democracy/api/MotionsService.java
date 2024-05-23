package be.tr.democracy.api;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.Optional;

public interface MotionsService {

    Page<Motion> findMotions(String searchTerm, PageRequest pageRequest);

    Optional<Motion> getMotion(String motionId);
}
