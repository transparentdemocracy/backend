package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static be.tr.democracy.inmem.DataModelMother.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DataModelMapperTest {

    // TODO: TEST DocumentReference parsing

    @Test
    void basicMapping() {
        //GIVEN
        final Map<String, PoliticianDTO> politicianDTO = politicianDTOMap();
        final List<VoteDTO> votes = twoMotionVotes();
        final var plenaries = buildPlenaries();
        final var dataModelMapper = new DataModelMapper(politicianDTO, votes, plenaries, new SummariesDTO(List.of()));

        //WHEN
        final var motions = dataModelMapper.buildAllMotionGroups();

        //THEN
        validate(motions);


    }

    private static void validate(List<MotionGroup> motionGroups) {
        assertThat(motionGroups, notNullValue());
        assertThat(motionGroups, hasSize(2));
        final var firstMotionGroup = motionGroups.get(0);
        final var motionsFirstGroup = firstMotionGroup.motions();
        final var firstMotion = motionsFirstGroup.get(0);
        assertThat(firstMotion.motionId(), is(MOTION_ID_1));
        assertThat(firstMotion.votingDate(), is(PLENARY_DATE_A));
        assertThat(firstMotion.descriptionNL(), is(MOTION_DESCRIPTION_1));
        assertThat(firstMotion.descriptionFR(), is(MOTION_DESCRIPTION_1));
        assertThat(firstMotion.titleNL(), is(PROPOSAL_TITLE_NL_1));
        assertThat(firstMotion.titleFR(), is(PROPOSAL_TITLE_FR_1));


        final var secondMotion = motionsFirstGroup.get(1);
        assertThat(secondMotion.motionId(), is(MOTION_ID_2));
        assertThat(secondMotion.votingDate(), is(PLENARY_DATE_A));
        assertThat(secondMotion.descriptionNL(), is(MOTION_DESCRIPTION_2));
        assertThat(secondMotion.descriptionFR(), is(MOTION_DESCRIPTION_2));
        assertThat(secondMotion.titleNL(), is(PROPOSAL_TITLE_NL_2));
        assertThat(secondMotion.titleFR(), is(PROPOSAL_TITLE_FR_2));

        final var secondmmotionGroup = motionGroups.get(1);
        final var motionsSecondGroup = secondmmotionGroup.motions();
        final var third = motionsSecondGroup.get(0);
        assertThat(third.motionId(), is(MOTION_ID_3));
        assertThat(third.votingDate(), is(PLENARY_DATE_B));
        assertThat(third.descriptionNL(), is(MOTION_DESCRIPTION_3));
        assertThat(third.descriptionFR(), is(MOTION_DESCRIPTION_3));
        assertThat(third.titleNL(), is(PROPOSAL_TITLE_NL_3));
        assertThat(third.titleFR(), is(PROPOSAL_TITLE_FR_3));

        final var fourth = motionsSecondGroup.get(1);
        assertThat(fourth.motionId(), is(MOTION_ID_4));
        assertThat(fourth.votingDate(), is(PLENARY_DATE_B));
        assertThat(fourth.descriptionNL(), is(MOTION_DESCRIPTION_4));
        assertThat(fourth.descriptionFR(), is(MOTION_DESCRIPTION_4));
        assertThat(fourth.titleNL(), is(PROPOSAL_TITLE_NL_4));
        assertThat(fourth.titleFR(), is(PROPOSAL_TITLE_FR_4));
    }


}