package be.tr.democracy.query;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


public class MotionsQuery implements MotionsService {

    private final MotionsReadModel motionsReadModel;
    private final Logger logger = LoggerFactory.getLogger(MotionsQuery.class);

    public MotionsQuery(MotionsReadModel motionsReadModel) {
        requireNonNull(motionsReadModel);
        this.motionsReadModel = motionsReadModel;
    }

    @Override
    public List<Motion> getMotions(int maximum) {
        final var sorted = getSorted(motionsReadModel.loadAll());
        logger.info("Loaded {} motions from database", sorted.size());
        if (maximum > 0 && sorted.size() > maximum)
            return sorted.subList(0, maximum);
        else return sorted;
    }

    private static List<Motion> getSorted(List<Motion> motions) {
        final var sorted = motions.stream().sorted((o1, o2) -> {
            final var first = LocalDate.parse(o1.date());
            final var second = LocalDate.parse(o2.date());
            return first.compareTo(second);
        }).collect(Collectors.toList());
        return sorted;
    }
}
