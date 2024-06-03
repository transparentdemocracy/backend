package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.page.PageRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataFileMotionsReadModelTest {

    private final DataFileMotionsReadModel readModel;

    public DataFileMotionsReadModelTest() {
        final var plenaryDTOFileLoader = new PlenaryDTOFileLoader("test-plenaries.json");
        readModel = new DataFileMotionsReadModel(
                plenaryDTOFileLoader,
                "test-votes.json",
                "test-politician.json",
                "test-summaries.json"
        );
    }

    @Test
    void loadMotionsOnStartUp() {

        final var motionGroups = readModel.loadAll();

        assertNotNull(motionGroups);
        assertEquals(6, motionGroups.size());

        final var motionGroup = motionGroups.getFirst();
        final var motion = motionGroup.motions().getFirst();
        assertEquals("55_160_mg_19_m0", motion.motionId());
        assertEquals("55_160", motion.plenaryId());
        assertEquals(0, motion.sequenceNumberInPlenary());
        assertNotNull(motion.descriptionNL());
        assertNotNull(motion.descriptionFR());
        assertNotNull(motion.titleFR());
        assertNotNull(motion.titleNL());
        assertNotNull(motion.newDocumentReference());
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
    void findMotionsWithUniCode() {
        final var motionPage = readModel.find("réunion", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(2, motionPage.values().size());
    }

    @Test
    void findMotionsWitMultipleSearchTerms() {
        final var motionPage = readModel.find("réunion Relations Ponthier", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(1, motionPage.values().size());
        assertEquals("55_160_mg_20",motionPage.values().getFirst().id());
    }

    @Test
    void findMotionsWithUkraïne() {
        final var motionPage = readModel.find("Oekraïne", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(1, motionPage.values().size());
        assertTrue(motionPage.values().getFirst().titleNL().contains("Oekraïne"));
    }

    @Test
    void findMotionsThroughDocumentSummary() {
        final var motionPage = readModel.find("elektrische", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(6, motionPage.values().size());
    }

    @Test
    void findAllMotions() {
        final var motionPage = readModel.find("", new PageRequest(1, 10));
        assertNotNull(motionPage);
        assertEquals(10, motionPage.pageSize());
        assertEquals(1, motionPage.pageNr());
        assertEquals(6, motionPage.values().size());
    }
}