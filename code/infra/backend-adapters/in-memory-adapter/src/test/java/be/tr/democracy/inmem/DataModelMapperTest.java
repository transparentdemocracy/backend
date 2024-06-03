package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static be.tr.democracy.inmem.DataModelMother.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DataModelMapperTest {

    @Test
    void basicMapping() {
        //GIVEN
        final var dataModelMapper = buildDataModelMapper();

        //WHEN
        final var motions = dataModelMapper.buildAllMotionGroups();

        //THEN
        validate(motions);
    }

    private static DataModelMapper buildDataModelMapper() {
        final Map<String, PoliticianDTO> politicianDTO = politicianDTOMap();
        final List<VoteDTO> votes = twoMotionVotes();
        final var plenaries = buildPlenaries();
        final var summaries = buildSummaries();
        return new DataModelMapper(politicianDTO, votes, plenaries, summaries);
    }

    private static List<SummaryDTO> buildSummaries() {
        // TODO: create summaries for testing
        return List.of();
    }

    private static void validate(List<MotionGroup> motionGroups) {
        assertThat(motionGroups, notNullValue());
        assertThat(motionGroups, hasSize(6));

        validateSortingOrder(motionGroups);

        final var firstMotionGroupA = motionGroups.get(2);
        validateFirstMotionGroupA(firstMotionGroupA);

        final var firstMotionGroupB = motionGroups.get(0);
        validateFirstMotionGroupB(firstMotionGroupB);

        final var secondMotionGroupB = motionGroups.get(1);
        validateSecondMotionGroupB(secondMotionGroupB);
    }

    private static void validateSecondMotionGroupB(MotionGroup secondMotionGroupB) {
        final var motionsSecondGroup = secondMotionGroupB.motions();
        final var third = motionsSecondGroup.get(0);
        assertThat(third.motionId(), is(MOTION_ID_3));
        assertThat(third.votingDate(), is(PLENARY_DATE_B_30_MAY));
        assertThat(third.descriptionNL(), is(MOTION_DESCRIPTION_3));
        assertThat(third.descriptionFR(), is(MOTION_DESCRIPTION_3));
        assertThat(third.titleNL(), is(MOTION_TITLE_NL_3));
        assertThat(third.titleFR(), is(MOTION_TITLE_FR_3));

        final var fourth = motionsSecondGroup.get(1);
        assertThat(fourth.motionId(), is(MOTION_ID_4));
        assertThat(fourth.votingDate(), is(PLENARY_DATE_B_30_MAY));
        assertThat(fourth.descriptionNL(), is(MOTION_DESCRIPTION_4));
        assertThat(fourth.descriptionFR(), is(MOTION_DESCRIPTION_4));
        assertThat(fourth.titleNL(), is(MOTION_TITLE_NL_4));
        assertThat(fourth.titleFR(), is(MOTION_TITLE_FR_4));
    }

    private static void validateFirstMotionGroupB(MotionGroup firstMotionGroupB) {
        final var motionsFirstMotionGroupB = firstMotionGroupB.motions();
        final var firstB = motionsFirstMotionGroupB.get(0);
        assertThat(firstB.motionId(), is(MOTION_ID_1));
        assertThat(firstB.votingDate(), is(PLENARY_DATE_B_30_MAY));
        assertThat(firstB.descriptionNL(), is(MOTION_DESCRIPTION_1));
        assertThat(firstB.descriptionFR(), is(MOTION_DESCRIPTION_1));
        assertThat(firstB.titleNL(), is(MOTION_TITLE_NL_1));
        assertThat(firstB.titleFR(), is(MOTION_TITLE_FR_1));

        final var secondB = motionsFirstMotionGroupB.get(1);
        assertThat(secondB.motionId(), is(MOTION_ID_2));
        assertThat(secondB.votingDate(), is(PLENARY_DATE_B_30_MAY));
        assertThat(secondB.descriptionNL(), is(MOTION_DESCRIPTION_2));
        assertThat(secondB.descriptionFR(), is(MOTION_DESCRIPTION_2));
        assertThat(secondB.titleNL(), is(MOTION_TITLE_NL_2));
        assertThat(secondB.titleFR(), is(MOTION_TITLE_FR_2));
    }

    private static void validateFirstMotionGroupA(MotionGroup firstMotionGroupA) {
        final var motionsFirstGroup = firstMotionGroupA.motions();
        final var firstMotion = motionsFirstGroup.get(0);
        assertThat(firstMotion.motionId(), is(MOTION_ID_1));
        assertThat(firstMotion.votingDate(), is(PLENARY_DATE_A_20_MAY));
        assertThat(firstMotion.descriptionNL(), is(MOTION_DESCRIPTION_1));
        assertThat(firstMotion.descriptionFR(), is(MOTION_DESCRIPTION_1));
        assertThat(firstMotion.titleNL(), is(MOTION_TITLE_NL_1));
        assertThat(firstMotion.titleFR(), is(MOTION_TITLE_FR_1));


        final var secondMotion = motionsFirstGroup.get(1);
        assertThat(secondMotion.motionId(), is(MOTION_ID_2));
        assertThat(secondMotion.votingDate(), is(PLENARY_DATE_A_20_MAY));
        assertThat(secondMotion.descriptionNL(), is(MOTION_DESCRIPTION_2));
        assertThat(secondMotion.descriptionFR(), is(MOTION_DESCRIPTION_2));
        assertThat(secondMotion.titleNL(), is(MOTION_TITLE_NL_2));
        assertThat(secondMotion.titleFR(), is(MOTION_TITLE_FR_2));
    }

    /**
     * Sorting is not really the responsibility from the data mapper.
     * But since for now everything is done in mem, this is good enough.
     * <p>
     * We want to have motionsGroups groups in the following order
     * latest plenary date first
     * for the same plenary, the motion groups in the sequence number where they were voted on
     *
     * @param motionGroups
     */
    private static void validateSortingOrder(List<MotionGroup> motionGroups) {
        var motionGroupIds = motionGroups.stream().map(MotionGroup::id).toList();
        assertThat(motionGroupIds, contains(
                "motion_group_disc_B_1",
                "motion_group_disc_B_2",
                "motion_group_disc_A_1",
                "motion_group_disc_A_2",
                "motion_group_disc_C_1",
                "motion_group_disc_C_2"
        ));
        motionGroups.forEach(motionGroup -> validateMotionOrder(motionGroup.motions()));
    }

    /**
     * We want to have motionsGroups in the order that they were voted on, for this we use the motion sequence number in ascending order.
     * <p>
     * Do NOT use the motionId for sorting.
     * <p>
     * Because this imposes a hidden contract on a motionId.
     * Even though we generate the id and to make it unique we have use some logic for it.
     * But make things explicit, not hidden in a simple string field.
     * An id could just as well be "Guido_is_een_toffe_jongen".
     * Which obviously is a great id.
     * But it does break any implicit contract, and is kind of hard to sort on. :-)
     **/
    private static void validateMotionOrder(List<Motion> motions) {
        final var motionSequenceNumbers = motions.stream().map(Motion::sequenceNumberInPlenary).toList();
        final var sorted = getSorted(motionSequenceNumbers);
        assertThat(motionSequenceNumbers, is(sorted));
    }

    private static List<Integer> getSorted(List<Integer> motionSequenceNumbers) {
        final var sorted = new ArrayList<Integer>(motionSequenceNumbers);
        sorted.sort(Comparator.naturalOrder());
        return sorted;
    }


}