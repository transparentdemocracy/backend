package be.tr.democracy.query;

import org.junit.jupiter.api.Test;

import static be.tr.democracy.query.ObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;

class MotionsQueryTest {


    @Test
    void loadMotions() {
        final var motionsQuery = new MotionsQuery(DummyMotionsReadModel.INSTANCE);

        final var motions = motionsQuery.findMotions(3);

        assertEquals(3, motions.size());
        assertTrue(motions.contains(FOURTH));
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(THIRD));
        assertFalse(motions.contains(SECOND));
    }

    @Test
    void loadAllMotions() {
        final var motionsQuery = new MotionsQuery(DummyMotionsReadModel.INSTANCE);

        final var motions = motionsQuery.findMotions(10);

        assertEquals(4, motions.size());
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
        assertTrue(motions.contains(FOURTH));
    }

    @Test
    void loadExactMotions() {
        final var motionsQuery = new MotionsQuery(DummyMotionsReadModel.INSTANCE);

        final var motions = motionsQuery.findMotions(4);

        assertEquals(4, motions.size());
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
        assertTrue(motions.contains(FOURTH));
    }


}