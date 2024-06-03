package be.tr.democracy.rest;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

class PlenaryViewMapperTest {
    @Test
    void basicMapping() {
        final var original = PlenaryMother.FIRST_PLENARY;
        final var plenaryViewDTO = PlenaryViewMapper.map(original);

        assertThat(plenaryViewDTO.id(), equalTo(original.id()));
        assertThat(plenaryViewDTO.id(), equalTo(original.id()));
        assertThat(plenaryViewDTO.htmlReportUrl(), equalTo(original.htmlReportUrl()));
        assertThat(plenaryViewDTO.pdfReportUrl(), equalTo(original.pdfReportUrl()));
        assertThat(plenaryViewDTO.motionGroups().size(), equalTo(2));
        assertThat(plenaryViewDTO.motionGroups().getFirst().motionLinks().size(), equalTo(2));

    }
}