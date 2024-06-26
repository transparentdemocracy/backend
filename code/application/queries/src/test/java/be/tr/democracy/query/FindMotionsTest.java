package be.tr.democracy.query;

import be.tr.democracy.vocabulary.page.PageRequest;
import org.junit.jupiter.api.Test;

import static be.tr.democracy.query.DummyMotionGroupReadModel.DUMMY_GROUP_1;
import static be.tr.democracy.query.DummyMotionGroupReadModel.DUMMY_GROUP_2;
import static be.tr.democracy.query.ObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;

class FindMotionsTest {


    @Test
    void loadMotions() {
        final var motionsQuery = new FindMotionsQuery(DummyMotionGroupReadModel.INSTANCE);

        final var page = motionsQuery.findMotions("", new PageRequest(1, 1));

        final var motions = page.values();
        assertEquals(1, motions.size());
        assertTrue(motions.contains(DUMMY_GROUP_1));
    }

    @Test
    void loadAllMotions() {
        final var motionsQuery = new FindMotionsQuery(DummyMotionGroupReadModel.INSTANCE);

        final var page = motionsQuery.findMotions("", new PageRequest(1, 4));
        final var motionGroups = page.values();
        assertEquals(2, motionGroups.size());
        final var firstMotionGroup = motionGroups.getFirst();
        final var firstMotions = firstMotionGroup.motions();
        assertEquals(2, firstMotions.size());
        assertTrue(firstMotions.contains(FIRST_MOTION));
        assertTrue(firstMotions.contains(SECOND_MOTION));
        final var secondMotionGroup = motionGroups.getLast();
        final var secondMotions = secondMotionGroup.motions();
        assertEquals(2, secondMotions.size());
        assertTrue(secondMotions.contains(THIRD_MOTION));
        assertTrue(secondMotions.contains(FOURTH_MOTION));
    }

    @Test
    void loadExactMotions() {
        final var motionsQuery = new FindMotionsQuery(DummyMotionGroupReadModel.INSTANCE);

        final var page = motionsQuery.findMotions("", new PageRequest(2, 2));
        final var motions = page.values();
        assertEquals(2, motions.size());
        assertTrue(motions.contains(DUMMY_GROUP_1));
        assertTrue(motions.contains(DUMMY_GROUP_2));
    }


}