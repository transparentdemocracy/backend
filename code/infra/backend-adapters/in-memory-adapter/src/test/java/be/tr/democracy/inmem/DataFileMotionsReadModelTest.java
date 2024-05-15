package be.tr.democracy.inmem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataFileMotionsReadModelTest {
    @Test
    void loadMotionsOnStartUp() {
        final var readModel = new DataFileMotionsReadModel(
                "test-plenaries.json",
                "test-votes.json",
                "test-politician.json");

        final var motions = readModel.loadAll();

        assertNotNull(motions);
        assertEquals(2, motions.size());

        final var motion = motions.getFirst();
        assertEquals("55_261_1", motion.motionId());
        assertEquals("55_261", motion.plenaryId());
        assertEquals(1, motion.numberInPlenary());
        assertNotNull(motion.descriptionNL());
        assertNotNull(motion.descriptionFR());
        assertNotNull(motion.titleFR());
        assertNotNull(motion.titleNL());
        assertNotNull(motion.documentReference());
        assertNotNull(motion.voteCount());
        assertEquals(1, motion.voteCount().noVotes().nrOfVotes());
        assertEquals(4, motion.voteCount().yesVotes().nrOfVotes());
        assertEquals(1, motion.voteCount().absentees().nrOfVotes());
    }
}