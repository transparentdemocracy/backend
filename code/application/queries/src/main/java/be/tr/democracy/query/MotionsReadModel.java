package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;

import java.util.List;

public interface MotionsReadModel {
    List<Motion> loadAll();
}
