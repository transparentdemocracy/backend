package be.tr.democracy.query;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;


public class MotionsQuery implements MotionsService {

    private static final Comparator<Motion> motionComparator = (o1, o2) -> {
        final var first = LocalDate.parse(o1.date());
        final var second = LocalDate.parse(o2.date());
        return second.compareTo(first);
    };

    private final MotionsReadModel motionsReadModel;
    private final Logger logger = LoggerFactory.getLogger(MotionsQuery.class);

    public MotionsQuery(MotionsReadModel motionsReadModel) {
        requireNonNull(motionsReadModel);
        this.motionsReadModel = motionsReadModel;
    }

    @Override
    public List<Motion> findMotions(int maximum) {
        final var motions = motionsReadModel.loadAll();
        logger.info("Loaded {} motions from database", motions.size());
        if (maximum > 0 && motions.size() > maximum)
            return motions.subList(0, maximum);
        else return motions;
    }

    @Override
    public Page<Motion> findMotions(String searchTerm, PageRequest pageRequest) {
        final var motionPage = motionsReadModel.find(searchTerm, pageRequest);
        final var sorted = motionPage.sortedPage(motionComparator);
        logger.info("Loaded page {} motions from database for searchTerm {}", sorted.pageNr(), searchTerm);
        return sorted;
    }

}
