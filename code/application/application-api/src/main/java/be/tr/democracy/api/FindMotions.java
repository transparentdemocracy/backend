package be.tr.democracy.api;

import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;

public interface FindMotions {

    Page<MotionGroup> findMotions(String searchTerm, PageRequest pageRequest);
}
