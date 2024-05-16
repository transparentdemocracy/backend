package be.tr.democracy.inmem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataModelMother {

    public static final String TRIPLE_D = "Triple D";
    public static final String SOCRATES = "Socrates";
    public static final String CRONOS = "Cronos";

    private DataModelMother() {
    }

    public static List<VoteDTO> exampleVoteDTO() {
        return List.of(
                new VoteDTO("politician_1", "motion_1", "yes"),
                new VoteDTO("politician_2", "motion_1", "yes"),
                new VoteDTO("politician_3", "motion_1", "no"),
                new VoteDTO("politician_4", "motion_1", "no"),
                new VoteDTO("politician_5", "motion_1", "no"),
                new VoteDTO("politician_6", "motion_1", "abstention")
        );
    }

    public static List<VoteDTO> exampleInvalidVoteDTO() {
        return List.of(
                new VoteDTO("politician_1", "motion_1", "yes"),
                new VoteDTO("politician_2", "motion_1", "yes"),
                new VoteDTO("politician_3", "motion_1", "badvote"),
                new VoteDTO("politician_4", "motion_1", "no"),
                new VoteDTO("politician_5", "motion_1", "abstention"),
                new VoteDTO("politician_6", "motion_1", "brol")
        );
    }

    public static List<VoteDTO> exampleWithDifferentMotionIDs() {
        return List.of(
                new VoteDTO("politician_1", "motion_1", "yes"),
                new VoteDTO("politician_2", "motion_2", "yes"),
                new VoteDTO("politician_3", "motion_3", "no"),
                new VoteDTO("politician_4", "motion_4", "no"),
                new VoteDTO("politician_5", "motion_1", "abstention"),
                new VoteDTO("politician_6", "motion_1", "abstention")
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
}
