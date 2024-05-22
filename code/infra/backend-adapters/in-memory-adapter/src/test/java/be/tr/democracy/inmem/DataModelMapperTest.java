package be.tr.democracy.inmem;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static be.tr.democracy.inmem.DataModelMother.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DataModelMapperTest {

    @Test
    void basicMapping() {
        final Map<String, PoliticianDTO> politicianDTO = politicianDTOMap();
        final List<VoteDTO> votes = twoMotionVotes();

        final var dataModelMapper = new DataModelMapper(politicianDTO, votes);

        final var plenaryToMap = buildPlenary();
        final var motions = dataModelMapper.buildMotions(plenaryToMap);

        assertThat(motions, notNullValue());
        assertThat(motions, hasSize(2));

        final var firstMotion = motions.getFirst();
        assertThat(firstMotion.motionId(), is(MOTION_ID_1));
        assertThat(firstMotion.date(), is(PLENARY_DATE));
        assertThat(firstMotion.descriptionNL(), is(MOTION_DESCRIPTION_1));
        assertThat(firstMotion.descriptionFR(), is(MOTION_DESCRIPTION_1));
        assertThat(firstMotion.titleNL(), is(MOTION_TITLE_NL_1));
        assertThat(firstMotion.titleFR(), is(MOTION_TITLE_FR_1));


        final var secondMotion = motions.getLast();
        assertThat(secondMotion.motionId(), is(MOTION_ID_2));
        assertThat(secondMotion.date(), is(PLENARY_DATE));
        assertThat(secondMotion.descriptionNL(), is(MOTION_DESCRIPTION_2));
        assertThat(secondMotion.descriptionFR(), is(MOTION_DESCRIPTION_2));
        assertThat(secondMotion.titleNL(), is(MOTION_TITLE_NL_2));
        assertThat(secondMotion.titleFR(), is(MOTION_TITLE_FR_2));


    }


}