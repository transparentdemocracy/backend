package be.tr.democracy.api;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.Optional;

public interface PlenaryService {

    Page<Plenary> findPlenaries(String searchTerm, PageRequest pageRequest);

    Optional<Plenary> getPlenary(String plenaryId);
}
