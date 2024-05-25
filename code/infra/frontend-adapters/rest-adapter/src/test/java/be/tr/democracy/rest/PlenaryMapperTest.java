package be.tr.democracy.rest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class PlenaryMapperTest {
    @Test
    void basicMapping() {
        final var original = PlenaryMother.FIRST_PLENARY;
        final var plenaryViewDTO = PlenaryMapper.map(original);

        assertThat(plenaryViewDTO.id(), equalTo(original.id()));
        assertThat(plenaryViewDTO.id(), equalTo(original.id()));
        assertThat(plenaryViewDTO.htmlReportUrl(), equalTo(original.htmlReportUrl()));
        assertThat(plenaryViewDTO.pdfReportUrl(), equalTo(original.pdfReportUrl()));
        assertThat(plenaryViewDTO.motions().size(), equalTo(original.motions().size()));

    }
}