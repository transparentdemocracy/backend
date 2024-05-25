package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.page.PageRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataFileMotionsReadModelTest {

    private final DataFileMotionsReadModel readModel;

    public DataFileMotionsReadModelTest() {
        final var plenaryDTOFileLoader = new PlenaryDTOFileLoader("test-plenaries.json");
        readModel = new DataFileMotionsReadModel(
                plenaryDTOFileLoader,
                "test-votes.json",
                "test-politician.json");
    }

    @Test
    void loadMotionsOnStartUp() {

        final var motionGroups = readModel.loadAll();

        assertNotNull(motionGroups);
        assertEquals(2, motionGroups.size());

        final var motionGroup = motionGroups.getFirst();
        final var motion = motionGroup.motions().getFirst();
        assertEquals("55_160_mg_19_m0", motion.motionId());
        assertEquals("55_160", motion.plenaryId());
        assertEquals(0, motion.sequenceNumberInPlenary());
        assertNotNull(motion.descriptionNL());
        assertNotNull(motion.descriptionFR());
        assertNotNull(motion.titleFR());
        assertNotNull(motion.titleNL());
        assertNotNull(motion.documentReference());
        assertNotNull(motion.voteCount());
        assertEquals(1, motion.voteCount().noVotes().nrOfVotes());
        assertEquals(1, motion.voteCount().yesVotes().nrOfVotes());
        assertEquals(1, motion.voteCount().abstention().nrOfVotes());
    }

    @Test
    void findMotions() {
        final var motionPage = readModel.find("interpellatie", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(2, motionPage.values().size());
    }

    @Test
    void findAllMotions() {
        final var motionPage = readModel.find("", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(2, motionPage.values().size());
    }
}