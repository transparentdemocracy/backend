package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class DataFilePlenaryReadModelTest {

    public static final PageRequest PAGE_REQUEST = new PageRequest(1, 10);
    private final DataFilePlenaryReadModel readModel;

    public DataFilePlenaryReadModelTest() {
        final var plenaryDTOFileLoader = new PlenaryDTOFileLoader("test-plenaries.json");
        readModel = DataFilePlenaryReadModel.create(plenaryDTOFileLoader);
    }

    @Test
    void getById() {

        final var plenaryPage = readModel.getPlenary("55_160");

        assertNotNull(plenaryPage);
        plenaryPage.ifPresentOrElse(new Consumer<Plenary>() {
            @Override
            public void accept(Plenary plenary) {
                assertThat(plenary.id(), is("55_160"));
                assertThat(plenary.legislature(), is("55"));
                assertThat(plenary.plenaryDate(), is("2022-02-03"));
            }
        }, Assertions::fail);

    }

    @Test
    void trimGetById() {

        final var plenaryPage = readModel.getPlenary(" 55_160 ");

        assertNotNull(plenaryPage);
        plenaryPage.ifPresentOrElse(new Consumer<Plenary>() {
            @Override
            public void accept(Plenary plenary) {
                assertThat(plenary.id(), is("55_160"));
                assertThat(plenary.legislature(), is("55"));
                assertThat(plenary.plenaryDate(), is("2022-02-03"));
            }
        }, Assertions::fail);

    }

    @Test
    void trimSearches() {

        final var plenaryPage = readModel.find(" 55_160 ", PAGE_REQUEST);

        assertNotNull(plenaryPage);
        assertEquals(10, plenaryPage.pageSize());
        assertEquals(1, plenaryPage.pageNr());
        assertEquals(1, plenaryPage.values().size());

        final var plenary = plenaryPage.values().getFirst();
        assertThat(plenary.id(), is("55_160"));
        assertThat(plenary.legislature(), is("55"));
        assertThat(plenary.plenaryDate(), is("2022-02-03"));

    }

    @Test
    void findById() {

        final var plenaryPage = readModel.find("55_160", PAGE_REQUEST);

        assertNotNull(plenaryPage);
        assertEquals(10, plenaryPage.pageSize());
        assertEquals(1, plenaryPage.pageNr());
        assertEquals(1, plenaryPage.values().size());

        final var plenary = plenaryPage.values().getFirst();
        assertThat(plenary.id(), is("55_160"));
        assertThat(plenary.legislature(), is("55"));
        assertThat(plenary.plenaryDate(), is("2022-02-03"));
    }

    @Test
    void findByMotionTitleSingleSearchTerm() {

        final var plenaryPage = readModel.find("Annick", PAGE_REQUEST);

        assertNotNull(plenaryPage);
        assertEquals(10, plenaryPage.pageSize());
        assertEquals(1, plenaryPage.pageNr());
        assertEquals(1, plenaryPage.values().size());

        final var plenary = plenaryPage.values().getFirst();
        assertThat(plenary.id(), is("55_160"));
        assertThat(plenary.legislature(), is("55"));
        assertThat(plenary.plenaryDate(), is("2022-02-03"));
    }

    @Test
    void trimFindByMotionTitleSingleSearchTerm() {

        final var plenaryPage = readModel.find("  Annick   ", PAGE_REQUEST);

        assertNotNull(plenaryPage);
        assertEquals(10, plenaryPage.pageSize());
        assertEquals(1, plenaryPage.pageNr());
        assertEquals(1, plenaryPage.values().size());

        final var plenary = plenaryPage.values().getFirst();
        assertThat(plenary.id(), is("55_160"));
        assertThat(plenary.legislature(), is("55"));
        assertThat(plenary.plenaryDate(), is("2022-02-03"));
    }

    @Test
    void findByMotionTitleMultipleSearchTerms() {

        final var plenaryPage = readModel.find("Annick Ponthier", PAGE_REQUEST);

        assertNotNull(plenaryPage);
        assertEquals(10, plenaryPage.pageSize());
        assertEquals(1, plenaryPage.pageNr());
        assertEquals(1, plenaryPage.values().size());

        final var plenary = plenaryPage.values().getFirst();
        assertThat(plenary.id(), is("55_160"));
        assertThat(plenary.legislature(), is("55"));
        assertThat(plenary.plenaryDate(), is("2022-02-03"));
    }

}
