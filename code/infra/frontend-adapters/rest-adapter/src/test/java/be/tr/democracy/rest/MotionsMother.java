package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.VoteCount;

import java.util.List;

public class MotionsMother {
    static final Motion FIRST = new Motion("first", "plenaryA", "2024-01-01", 1, "NL Title 1", "FR Title 1", "2459/1-1", "De eerste", "Le premier", new VoteCount(5, 2, 3));
    static final Motion SECOND = new Motion("second", "plenaryA", "2024-01-01", 1, "NL Title 2", "FR Title 2", "2459/1-2", "De tweede", "Le deuxieme", new VoteCount(5, 2, 3));
    static final Motion THIRD = new Motion("third", "plenaryB", "2024-01-02", 1, "NL Title 3", "FR Title 3", "2459/1-3", "De derde", "Le troisieme", new VoteCount(5, 2, 3));
    static final Motion FOURTH = new Motion("fourth", "plenaryB", "2024-01-03", 1, "NL Title 4", "FR Title 4", "2459/1-4", "De vierde", "Le quatrieme", new VoteCount(5, 2, 3));
    static final List<Motion> DUMMY_MOTIONS = List.of(
            FIRST,
            SECOND,
            THIRD,
            FOURTH
    );
    static final List<Motion> THREE_MOTIONS = List.of(
            FIRST,
            SECOND,
            THIRD
    );

    private MotionsMother() {
    }
}
