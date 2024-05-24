package be.tr.democracy.inmem;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataModelMother {

    public static final String TRIPLE_D = "Triple D";
    public static final String SOCRATES = "Socrates";
    public static final String CRONOS = "Cronos";
    public static final String PROPOSAL_DISCUSSION_1 = "proposal_discussion_1";
    public static final String PROPOSAL_DISCUSSION_2 = "proposal_discussion_2";
    public static final String PROPOSAL_ID_1 = "proposal_1";
    public static final String PROPOSAL_ID_2 = "proposal_2";
    public static final String PROPOSAL_ID_3 = "proposal_3";
    public static final String PROPOSAL_ID_4 = "proposal_4";
    public static final String VOTING_ID_MOTION_1 = "voting_id_motion_1";
    public static final String VOTING_ID_MOTION_2 = "voting_id_motion_2";
    public static final String MOTION_ID_1 = "motion_id_1";
    public static final String MOTION_ID_2 = "motion_id_2";
    public static final String MOTION_ID_3 = "motion_id_3";
    public static final String MOTION_ID_4 = "motion_id_4";
    public static final String PLENARY_DATE_A = "22-05-2024";
    public static final String PLENARY_DATE_B = "30-05-2024";
    public static final String MOTION_DESCRIPTION_1 = "motion_description_1";
    public static final String MOTION_DESCRIPTION_2 = "motion_description_2";
    public static final String MOTION_DESCRIPTION_3 = "motion_description_3";
    public static final String MOTION_DESCRIPTION_4 = "motion_description_4";
    public static final String MOTION_TITLE_NL_1 = "motion_title_nl_1";
    public static final String MOTION_TITLE_FR_1 = "motion_title_fr_1";
    public static final String MOTION_TITLE_NL_2 = "motion_title_nl_2";
    public static final String MOTION_TITLE_FR_2 = "motion_title_fr_2";
    public static final String MOTION_TITLE_NL_3 = "motion_title_nl_3";
    public static final String MOTION_TITLE_FR_3 = "motion_title_fr_3";
    public static final String MOTION_TITLE_NL_4 = "motion_title_nl_4";
    public static final String MOTION_TITLE_FR_4 = "motion_title_fr_4";
    public static final String PROPOSAL_TITLE_NL_1 = "proposal_title_nl_1";
    public static final String PROPOSAL_TITLE_FR_1 = "proposal_title_fr_1";
    public static final String PROPOSAL_TITLE_NL_2 = "proposal_title_nl_2";
    public static final String PROPOSAL_TITLE_FR_2 = "proposal_title_fr_2";
    public static final String PROPOSAL_TITLE_NL_3 = "proposal_title_nl_3";
    public static final String PROPOSAL_TITLE_FR_3 = "proposal_title_fr_3";
    public static final String PROPOSAL_TITLE_NL_4 = "proposal_title_nl_4";
    public static final String PROPOSAL_TITLE_FR_4 = "proposal_title_fr_4";

    private DataModelMother() {
    }

    public static List<VoteDTO> exampleVoteDTO() {
        return exampleVotes(VOTING_ID_MOTION_1);
    }

    public static List<VoteDTO> twoMotionVotes() {
        return Stream.of(
                        exampleVotes(VOTING_ID_MOTION_1),
                        exampleVotes(VOTING_ID_MOTION_2))
                .flatMap(Collection::stream)
                .toList();
    }

    public static List<VoteDTO> exampleInvalidVoteDTO() {
        return List.of(
                new VoteDTO("politician_1", VOTING_ID_MOTION_1, "yes"),
                new VoteDTO("politician_2", VOTING_ID_MOTION_1, "yes"),
                new VoteDTO("politician_3", VOTING_ID_MOTION_1, "badvote"),
                new VoteDTO("politician_4", VOTING_ID_MOTION_1, "no"),
                new VoteDTO("politician_5", VOTING_ID_MOTION_1, "abstention"),
                new VoteDTO("politician_6", VOTING_ID_MOTION_1, "brol")
        );
    }

    public static List<VoteDTO> exampleWithDifferentMotionIDs() {
        return List.of(
                new VoteDTO("politician_1", VOTING_ID_MOTION_1, "yes"),
                new VoteDTO("politician_2", "motion_2", "yes"),
                new VoteDTO("politician_3", "motion_3", "no"),
                new VoteDTO("politician_4", "motion_4", "no"),
                new VoteDTO("politician_5", VOTING_ID_MOTION_1, "abstention"),
                new VoteDTO("politician_6", VOTING_ID_MOTION_1, "abstention")
        );
    }

    public static List<PoliticianDTO> examplePoliticiansDTO() {
        return List.of(
                new PoliticianDTO("politician_1", "Guido", TRIPLE_D),
                new PoliticianDTO("politician_2", "Franky", TRIPLE_D),
                new PoliticianDTO("politician_3", "Karel", SOCRATES),
                new PoliticianDTO("politician_4", "Tim", SOCRATES),
                new PoliticianDTO("politician_5", "Gert", TRIPLE_D),
                new PoliticianDTO("politician_6", "Jefke", CRONOS)
        );
    }

    public static Map<String, PoliticianDTO> politicianDTOMap() {
        return examplePoliticiansDTO().stream().collect(Collectors.toMap(PoliticianDTO::id, x -> x));
    }

    static PlenaryDTO buildPlenary() {
        final var proposalDiscussions = buildProposalDiscussions();
        final List<MotionGroupDTO> motionGroups = buildMotionGroupsA();
        return new PlenaryDTO("Plenary_1",
                1, 55, PLENARY_DATE_A,
                "pdf_url", "html_url", proposalDiscussions, motionGroups


        );
    }

    /**
     * Build two plenaries, the first having four proposals, the second none.
     * But the motions form the second refer to the proposals from the first
     *
     * @return
     */
    static List<PlenaryDTO> buildPlenaries() {
        final var proposalDiscussions = buildProposalDiscussions();
        return
                List.of(
                        new PlenaryDTO("Plenary_1",
                                1, 55, PLENARY_DATE_A,
                                "pdf_url", "html_url", proposalDiscussions, buildMotionGroupsA()),
                        new PlenaryDTO("Plenary_2",
                                1, 55, PLENARY_DATE_B,
                                "pdf_url", "html_url", List.of(), buildMotionGroupsB()));
    }

    private static List<VoteDTO> exampleVotes(String votingIdMotion1) {
        return List.of(
                new VoteDTO("politician_1", votingIdMotion1, "yes"),
                new VoteDTO("politician_2", votingIdMotion1, "yes"),
                new VoteDTO("politician_3", votingIdMotion1, "no"),
                new VoteDTO("politician_4", votingIdMotion1, "no"),
                new VoteDTO("politician_5", votingIdMotion1, "no"),
                new VoteDTO("politician_6", votingIdMotion1, "abstention")
        );
    }

    private static List<MotionGroupDTO> buildMotionGroupsA() {
        return List.of(new MotionGroupDTO("motion_group_disc_1", 1,
                "motion_group_title_nl_1",
                "motion_group_title_fr_1",
                "motion_group_docu_ref_1",
                PROPOSAL_DISCUSSION_1,
                buildFirstMotionDTOs()));
    }

    private static List<MotionGroupDTO> buildMotionGroupsB() {
        return List.of(new MotionGroupDTO("motion_group_disc_B", 1,
                "motion_group_title_nl_B",
                "motion_group_title_fr_B",
                "motion_group_docu_ref_B",
                PROPOSAL_DISCUSSION_2,
                buildSecondMotionDTOs()));
    }

    private static List<MotionDTO> buildFirstMotionDTOs() {
        return List.of(
                new MotionDTO(MOTION_ID_1, 1,
                        MOTION_TITLE_NL_1,
                        MOTION_TITLE_FR_1,
                        MOTION_DESCRIPTION_1,
                        "motion_docu_reference_1",
                        VOTING_ID_MOTION_1, PROPOSAL_ID_1, false

                ),

                new MotionDTO(MOTION_ID_2, 1,
                        MOTION_TITLE_NL_2,
                        MOTION_TITLE_FR_2,
                        MOTION_DESCRIPTION_2,
                        "motion_docu_reference_2",
                        VOTING_ID_MOTION_2, PROPOSAL_ID_2, false

                ));
    }

    private static List<MotionDTO> buildSecondMotionDTOs() {
        return List.of(
                new MotionDTO(MOTION_ID_3, 1,
                        MOTION_TITLE_NL_3,
                        MOTION_TITLE_FR_3,
                        MOTION_DESCRIPTION_3,
                        "motion_docu_reference_3",
                        VOTING_ID_MOTION_1, PROPOSAL_ID_3, false

                ),

                new MotionDTO(MOTION_ID_4, 1,
                        MOTION_TITLE_NL_4,
                        MOTION_TITLE_FR_4,
                        MOTION_DESCRIPTION_4,
                        "motion_docu_reference_4",
                        VOTING_ID_MOTION_2, PROPOSAL_ID_4, false

                ));
    }

    private static List<ProposalDiscussionDTO> buildProposalDiscussions() {
        return List.of(new ProposalDiscussionDTO(
                PROPOSAL_DISCUSSION_1, 1, "plenary_id_1",
                "plenary_description_nl",
                List.of("description_nl_tag_1", "description_nl_tag_2"),
                "plenary_description_fr",
                List.of("description_fr_tag_1", "description_fr_tag_2"),
                buildProposalDTOs()

        ));
    }

    private static List<ProposalDTO> buildProposalDTOs() {
        return List.of(
                new ProposalDTO(PROPOSAL_ID_1,
                        "document_reference_1",
                        PROPOSAL_TITLE_NL_1,
                        PROPOSAL_TITLE_FR_1
                ), new ProposalDTO("proposal_2",
                        "document_reference_2",
                        PROPOSAL_TITLE_NL_2,
                        PROPOSAL_TITLE_FR_2
                ), new ProposalDTO(PROPOSAL_ID_3,
                        "document_reference_3",
                        PROPOSAL_TITLE_NL_3,
                        PROPOSAL_TITLE_FR_3
                ), new ProposalDTO(PROPOSAL_ID_4,
                        "document_reference_4",
                        PROPOSAL_TITLE_NL_4,
                        PROPOSAL_TITLE_FR_4
                )


        );
    }


}
