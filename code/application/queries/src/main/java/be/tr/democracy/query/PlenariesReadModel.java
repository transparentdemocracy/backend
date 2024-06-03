package be.tr.democracy.query;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.Optional;

public interface PlenariesReadModel {

    Page<Plenary> find(String searchTerm, PageRequest pageRequest);

    Optional<Plenary> getPlenary(String plenary);
}
