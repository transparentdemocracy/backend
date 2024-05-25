package be.tr.democracy.query;

import be.tr.democracy.vocabulary.page.PageRequest;
import org.junit.jupiter.api.Test;

import static be.tr.democracy.query.ObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;

class MotionsQueryTest {


    @Test
    void loadMotions() {
        final var motionsQuery = new MotionsQuery(DummyMotionsReadModel.INSTANCE);

        final var page = motionsQuery.findMotions("", new PageRequest(1, 3));

        final var motions = page.values();
        assertEquals(3, motions.size());
        assertTrue(motions.contains(FOURTH));
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(THIRD));
        assertFalse(motions.contains(SECOND));
    }

    @Test
    void loadAllMotions() {
        final var motionsQuery = new MotionsQuery(DummyMotionsReadModel.INSTANCE);

        final var page = motionsQuery.findMotions("", new PageRequest(1, 4));
        final var motions = page.values();
        assertEquals(4, motions.size());
        assertTrue(motions.contains(FIRST));
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
        assertTrue(motions.contains(FOURTH));
    }

    @Test
    void loadExactMotions() {
        final var motionsQuery = new MotionsQuery(DummyMotionsReadModel.INSTANCE);

        final var page = motionsQuery.findMotions("", new PageRequest(2, 2));
        final var motions = page.values();
        assertEquals(2, motions.size());
        assertTrue(motions.contains(SECOND));
        assertTrue(motions.contains(THIRD));
    }


}