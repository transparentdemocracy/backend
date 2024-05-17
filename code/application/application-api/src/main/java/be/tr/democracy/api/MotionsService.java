package be.tr.democracy.api;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.List;

public interface MotionsService {

    List<Motion> findMotions(int maximum);

    Page<Motion> findMotions(String searchTerm, PageRequest pageRequest);
}
