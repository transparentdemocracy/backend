package be.tr.democracy.inmem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONDataFileLoaderTest {
    //TODO add test for summaries loading

    @Test
    public void parseTestPlenaries() {

        final var fileName = "test-plenaries.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var plenaryDTOS = jsonPlenariesLoader.loadPlenaries(fileName);
        assertNotNull(plenaryDTOS);
        assertEquals(4, plenaryDTOS.size());

        final var first = plenaryDTOS.getFirst();
        assertEquals(3, first.proposal_discussions().size());
        assertEquals(6, first.motion_groups().size());
        assertEquals(2, first.proposal_discussions().getFirst().proposals().size());
        assertEquals(2, first.motion_groups().getFirst().motions().size());
    }

    @Test
    public void parseTestVotes() {

        final var fileName = "test-votes.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var voteDTOS = jsonPlenariesLoader.loadVotes(fileName);
        assertNotNull(voteDTOS);
        assertEquals(10, voteDTOS.size());

        final var first = voteDTOS.getFirst();
        assertEquals("7220", first.politician_id());
        assertEquals("55_160_v1", first.voting_id());
        assertEquals("YES", first.vote_type());
    }

    @Test
    public void parseTestPolitician() {

        final var fileName = "test-politician.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var politicianDTOS = jsonPlenariesLoader.loadPolitician(fileName);
        assertNotNull(politicianDTOS);
        assertEquals(6, politicianDTOS.size());

        final var first = politicianDTOS.get("7220");
        assertEquals("7220", first.id());
        assertEquals("Moyaers Bert", first.full_name());
        assertEquals("Vooruit", first.party());
    }

    @Test
    public void parseTestSummaries() {

        final var fileName = "test-summaries.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var summaryDTOS = jsonPlenariesLoader.loadSummaries(fileName);
        assertNotNull(summaryDTOS);
        assertEquals(4, summaryDTOS.size());

        final var summaryDTO = summaryDTOS.get(3);
        assertEquals("1338/1", summaryDTO.document_id());
        assertEquals("blabla over elektrische snelle fietsen", summaryDTO.summary_nl());
        assertEquals("speed pedelecs", summaryDTO.summary_fr());

    }


}