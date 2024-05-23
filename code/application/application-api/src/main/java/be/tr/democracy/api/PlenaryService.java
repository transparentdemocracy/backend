package be.tr.democracy.api;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import be.tr.democracy.vocabulary.Plenary;

import java.util.Optional;

public interface PlenaryService {

    Page<Plenary> findPlenaries(String searchTerm, PageRequest pageRequest);

    Optional<Plenary> getPlenary(String plenaryId);
}
