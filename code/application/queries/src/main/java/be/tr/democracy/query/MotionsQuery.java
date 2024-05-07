package be.tr.democracy.query;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


public class MotionsQuery implements MotionsService {

    private final MotionsReadModel motionsReadModel;

    public MotionsQuery(MotionsReadModel motionsReadModel) {
        requireNonNull(motionsReadModel);
        this.motionsReadModel = motionsReadModel;
    }

    @Override
    public List<Motion> getMotions(int maximum) {
        final var motions = motionsReadModel.loadAll();
        final var sorted = motions.stream().sorted((o1, o2) -> {
            final var first = LocalDate.parse(o1.date());
            final var second = LocalDate.parse(o2.date());
            return first.compareTo(second);
        }).collect(Collectors.toList());

        if (sorted.size() > maximum)
            return sorted.subList(0, maximum);
        else return sorted;
    }
}
