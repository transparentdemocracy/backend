package be.tr.democracy.inmem;

import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

public class DataFileMotionsReadModel implements MotionsReadModel {
    private final Logger logger = LoggerFactory.getLogger(DataFileMotionsReadModel.class);
    private final List<Motion> allMotionsReadModel;

    public DataFileMotionsReadModel(String plenariesFileName, String votesFileName, String politiciansFileName) {
        this.allMotionsReadModel = MotionsReadModelFactory.INSTANCE.create(plenariesFileName, votesFileName, politiciansFileName);
        logger.info("Read Models build.");
    }

    @Override
    public List<Motion> loadAll() {
        return allMotionsReadModel;
    }

    @Override
    public Page<Motion> find(String searchTerm, PageRequest pageRequest) {
        final var motions = findMotions(searchTerm);
        return Page.slicePageFromList(pageRequest, motions);
    }

    private static Predicate<Motion> createFilter(String searchTerm) {

        return x -> containsSearchTerm(x.titleNL(), searchTerm) ||
                containsSearchTerm(x.titleFR(), searchTerm) ||
                containsSearchTerm(x.descriptionFR(), searchTerm) ||
                containsSearchTerm(x.descriptionNL(), searchTerm);
    }

    private static boolean containsSearchTerm(String subject, String searchTerm) {

        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private List<Motion> findMotions(String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return allMotionsReadModel
                    .stream()
                    .filter(createFilter(searchTerm)).toList();
        } else
            return allMotionsReadModel;
    }


}
