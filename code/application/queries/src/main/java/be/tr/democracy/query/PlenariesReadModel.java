package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import be.tr.democracy.vocabulary.Plenary;

import java.util.Optional;

public interface PlenariesReadModel {

    Page<Plenary> find(String date, PageRequest pageRequest);

    Optional<Plenary> getPlenary(String plenary);
}
