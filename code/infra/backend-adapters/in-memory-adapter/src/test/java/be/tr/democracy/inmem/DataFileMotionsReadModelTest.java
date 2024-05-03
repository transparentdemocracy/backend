package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.Motion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataFileMotionsReadModelTest {
    @Test
    void loadMotionsOnStartUp() {
        final var readModel = new DataFileMotionsReadModel("test-plenaries.json",
                "test-votes.json",
                "test-politician.json");

        final var motions = readModel.loadAll();

        assertNotNull(motions);
        assertEquals(24, motions.size());

        final var motion = motions.getFirst();
        assertEquals("55_160_1",motion.motionId());
        assertEquals("55_160",motion.plenaryId());
        assertEquals(1,motion.numberInPlenary());
        assertNotNull(motion.description());
        assertNotNull(motion.voteCount());
        assertEquals(1,motion.voteCount().nrOfNoVotes());
        assertEquals(4,motion.voteCount().nrOfYesVotes());
        assertEquals(1,motion.voteCount().nrOfAbsentees());
    }
}