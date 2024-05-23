package be.tr.democracy.query;

import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import be.tr.democracy.vocabulary.Plenary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Objects.requireNonNull;


public class PlenariesQuery implements PlenaryService {

    private static final Comparator<Plenary> plenaryComparator = (o1, o2) -> {
        final var first = LocalDate.parse(o1.date());
        final var second = LocalDate.parse(o2.date());
        return second.compareTo(first);
    };

    private final PlenariesReadModel plenariesReadModel;
    private final Logger logger = LoggerFactory.getLogger(PlenariesQuery.class);

    public PlenariesQuery(PlenariesReadModel plenariesReadModel) {
        requireNonNull(plenariesReadModel);
        this.plenariesReadModel = plenariesReadModel;
    }


    @Override
    public Page<Plenary> findPlenaries(String searchTerm, PageRequest pageRequest) {
        final var plenaryPage = plenariesReadModel.find(searchTerm, pageRequest);
        final var sorted = plenaryPage.sortedPage(plenaryComparator);
        logger.info("Loaded page {} plenaries from database for searchTerm {}", sorted.pageNr(), searchTerm);
        return sorted;
    }

    @Override
    public Optional<Plenary> getPlenary(String plenaryId) {
        return plenariesReadModel.getPlenary(plenaryId);
    }

}
