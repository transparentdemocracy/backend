package be.tr.democracy.inmem;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class StringDateComparatorTest {
    @Test
    void basicCompare() {

        assertThat(StringDateComparator.INSTANCE.compare("2020-01-01", "2020-01-01"), Matchers.is(0));
        assertThat(StringDateComparator.INSTANCE.compare("2020-01-01", "2020-01-02"), Matchers.is(1));
        assertThat(StringDateComparator.INSTANCE.compare("2020-01-01", "2019-01-02"), Matchers.is(-1));
    }
}