package be.tr.democracy.query;

import org.junit.jupiter.api.Test;

import java.util.List;

import static be.tr.democracy.query.ObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;

class MotionsQueryTest {

    private static final MotionsReadModel MOTIONS_READ_MODEL = () -> List.of(
            ObjectMother.FOURTH,
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