package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;

import java.util.List;

public interface MotionsReadModel {
    List<Motion> loadAll();

    Page<Motion> find(String searchTerm, PageRequest pageRequest);
}
