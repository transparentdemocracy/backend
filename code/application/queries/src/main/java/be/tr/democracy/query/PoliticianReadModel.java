package be.tr.democracy.query;

import be.tr.democracy.vocabulary.plenary.Politician;

import java.util.List;

public interface PoliticianReadModel {

    List<Politician> findAll();
}
