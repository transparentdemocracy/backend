package be.tr.democracy.vocabulary;

import be.tr.democracy.vocabulary.page.PageRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageRequestTest {

    @Test
    void cannotPageRequestWithNegativeNumbers() {

        assertThrows(IllegalArgumentException.class, () -> new PageRequest(1, -1));
        assertThrows(IllegalArgumentException.class, () -> new PageRequest(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new PageRequest(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> new PageRequest(2, 0));
        assertDoesNotThrow(() -> new PageRequest(2, 2));
    }
}