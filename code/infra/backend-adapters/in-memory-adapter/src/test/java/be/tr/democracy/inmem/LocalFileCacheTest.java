package be.tr.democracy.inmem;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LocalFileCacheTest {

    private CountingDummySupplier source;
    private LocalFileCache<PlenaryDTO> fileCache;

    @BeforeEach
    void setUp() {
        source = new CountingDummySupplier();
        fileCache = createLocalFileCache(source);
    }

    @AfterEach
    void tearDown() {
        fileCache.clear();
    }

    @Test
    void loadFromOriginalFileIfNoCache() {

        final var plenaryDTOS = fileCache.get();

        final var expected = source.getSuppliedPlenaryDTOS();
        assertThat(plenaryDTOS, Matchers.notNullValue());
        assertThat(source.nrOfInvocations(), is(1));
        assertThat(plenaryDTOS.size(), is(expected.size()));
        assertThat(plenaryDTOS, is(expected));
    }

    @Test
    void loadFromCacheTheSecondTime() {

        fileCache.get();
        final var plenaryDTOS = fileCache.get();

        final var expected = source.getSuppliedPlenaryDTOS();
        assertThat(plenaryDTOS, Matchers.notNullValue());
        assertThat(source.nrOfInvocations(), is(1));
        assertThat(plenaryDTOS.size(), is(expected.size()));
        assertThat(plenaryDTOS, is(expected));
    }

    private static LocalFileCache<PlenaryDTO> createLocalFileCache(CountingDummySupplier source) {
        return new LocalFileCache<PlenaryDTO>(source, new File("./target/fileCache"), "testFileCache", PlenaryDTO.class);
    }

}