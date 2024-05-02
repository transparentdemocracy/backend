package be.tr.democracy.inmem;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONPlenariesLoaderTest {

    @Test
    public void parseTestPlenaries() throws URISyntaxException, IOException {

        final var fileName = "test-plenaries.json";
        final var jsonPlenariesLoader = new JSONPlenariesLoader();

        final var plenaryDTOS = jsonPlenariesLoader.load(fileName);
        assertNotNull(plenaryDTOS);
        assertEquals(2, plenaryDTOS.size());

        final var first = plenaryDTOS.getFirst();
        assertEquals(5, first.proposals().size());
        assertEquals(5, first.motions().size());
    }


}