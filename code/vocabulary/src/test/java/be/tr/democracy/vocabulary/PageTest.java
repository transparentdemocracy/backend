package be.tr.democracy.vocabulary;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static be.tr.democracy.vocabulary.ObjectMother.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class PageTest {
    @Test
    void cannotPageWithNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new Page<>(1, -1, 20, List.of(FIRST, SECOND, THIRD, FOURTH)));
        assertThrows(IllegalArgumentException.class, () -> new Page<>(0, 0, 20, List.of(FIRST, SECOND, THIRD, FOURTH)));
        assertThrows(IllegalArgumentException.class, () -> new Page<>(-1, 1, 20, List.of(FIRST, SECOND, THIRD, FOURTH)));
        assertThrows(IllegalArgumentException.class, () -> new Page<>(2, 0, 20, List.of(FIRST, SECOND, THIRD, FOURTH)));
        assertDoesNotThrow(() -> new Page<>(2, 2, 20, List.of(FIRST, SECOND, THIRD, FOURTH)));
    }

    @Test
    void pageNrCannotBeHigherThanTotalPages() {
        assertEquals(20, new Page<>(25, 10, 20, List.of(FIRST, SECOND, THIRD, FOURTH)).pageNr());
    }

    @Test
    void sliceToPage() {
        final var list = IntStream.range(0, 20)
                .boxed()
                .toList();
        final var page = Page.slicePageFromList(new PageRequest(2, 5), list);

        assertNotNull(page);
        assertThat(page.pageSize(), is(5));
        assertThat(page.totalPages(), is(4));
        assertThat(page.pageNr(), is(2));
    }
}