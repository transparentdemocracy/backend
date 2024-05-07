package be.tr.democracy.inmem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONDataFileLoaderTest {

    @Test
    public void parseTestPlenaries() {

        final var fileName = "test-plenaries.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var plenaryDTOS = jsonPlenariesLoader.loadPlenaries(fileName);
        assertNotNull(plenaryDTOS);
        assertEquals(2, plenaryDTOS.size());

        final var first = plenaryDTOS.getFirst();
        assertEquals(5, first.proposals().size());
        assertEquals(5, first.motions().size());
    }

    @Test
    public void parseTestVotes() {

        final var fileName = "test-votes.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var voteDTOS = jsonPlenariesLoader.loadVotes(fileName);
        assertNotNull(voteDTOS);
        assertEquals(6, voteDTOS.size());

        final var first = voteDTOS.getFirst();
        assertEquals( "7124",first.politician_id());
        assertEquals("55_160_1", first.motion_id());
        assertEquals("YES", first.vote_type());
    }

    @Test
    public void parseTestPolitician() {

        final var fileName = "test-politician.json";
        final var jsonPlenariesLoader = new JSONDataFileLoader();

        final var politicianDTOS = jsonPlenariesLoader.loadPolitician(fileName);
        assertNotNull(politicianDTOS);
        assertEquals(6, politicianDTOS.size());

        final var first = politicianDTOS.getFirst();
        assertEquals(7220, first.id());
        assertEquals("Moyaers Bert", first.full_name());
        assertEquals("Vooruit", first.party());
    }


}