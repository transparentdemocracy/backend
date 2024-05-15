package be.tr.democracy.vocabulary;

import org.junit.jupiter.api.Test;

import java.util.List;

import static be.tr.democracy.vocabulary.ObjectMother.*;
import static org.junit.jupiter.api.Assertions.*;

class PageTest {
    @Test
    void cannotPageWithNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new Page<>(1, -1, 20, List.of(FIRST,SECOND,THIRD,FOURTH)));
        assertThrows(IllegalArgumentException.class, () -> new Page<>(0, 0, 20, List.of(FIRST,SECOND,THIRD,FOURTH)));
        assertThrows(IllegalArgumentException.class, () -> new Page<>(-1, 1, 20, List.of(FIRST,SECOND,THIRD,FOURTH)));
        assertThrows(IllegalArgumentException.class, () -> new Page<>(2, 0, 20, List.of(FIRST,SECOND,THIRD,FOURTH)));
        assertDoesNotThrow(() -> new Page<>(2, 2, 20, List.of(FIRST,SECOND,THIRD,FOURTH)));
    }

    @Test
    void pageNrCannotBeHigherThanTotalPages() {
        assertEquals(20, new Page<>(25, 10, 20, List.of(FIRST,SECOND,THIRD,FOURTH)).pageNr());
    }
}