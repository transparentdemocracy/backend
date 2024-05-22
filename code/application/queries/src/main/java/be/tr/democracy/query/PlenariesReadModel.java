package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.Optional;

public interface MotionsReadModel {

    Page<Motion> find(String searchTerm, PageRequest pageRequest);

    Optional<Motion> getMotion(String motionId);
}
