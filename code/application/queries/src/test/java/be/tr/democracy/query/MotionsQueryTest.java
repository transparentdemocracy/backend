package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.VoteCount;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MotionsQueryTest {

    private static final Motion FIRST = new Motion("first", "plenaryA", "2024-01-01", 1, "NL Title 1", "FR Title 1", "2459/1-1", "De eerste","Le premier", new VoteCount(5, 2, 3));
    private static final Motion SECOND = new Motion("second", "plenaryA", "2024-01-01", 1,"NL Title 2", "FR Title 2", "2459/1-2", "De tweede","Le deuxieme", new VoteCount(5, 2, 3));
    private static final Motion THIRD = new Motion("third", "plenaryB", "2024-01-02", 1, "NL Title 3", "FR Title 3", "2459/1-3","De derde","Le troisieme", new VoteCount(5, 2, 3));
    private static final Motion FOURTH = new Motion("fourth", "plenaryB", "2024-01-03", 1, "NL Title 4", "FR Title 4", "2459/1-4","De vierde", "Le quatrieme",new VoteCount(5, 2, 3));
    private static final MotionsReadModel MOTIONS_READ_MODEL = () -> List.of(
            FOURTH,
            FIRST,
            THIRD,
            SECOND
    );

    @Test
    void loadMotions() {
        final var motionsQuery = new MotionsQuery(MOTIONS_READ_MODEL);

        final var motions = motionsQuery.getMotions(3);

        assertEquals(3, motions.size());
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
        assertFalse(motions.contains(FOURTH));
    }

    @Test
    void loadAllMotions() {
        final var motionsQuery = new MotionsQuery(MOTIONS_READ_MODEL);

        final var motions = motionsQuery.getMotions(10);

        assertEquals(4, motions.size());
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
        assertTrue(motions.contains(FOURTH));
    }

    @Test
    void loadExactMotions() {
        final var motionsQuery = new MotionsQuery(MOTIONS_READ_MODEL);

        final var motions = motionsQuery.getMotions(4);

        assertEquals(4, motions.size());
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
        assertTrue(motions.contains(FOURTH));
    }
}