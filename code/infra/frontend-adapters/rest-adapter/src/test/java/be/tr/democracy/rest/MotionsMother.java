package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.motion.PartyVotes;
import be.tr.democracy.vocabulary.motion.VoteCount;
import be.tr.democracy.vocabulary.motion.Votes;

import java.util.List;

public class MotionsMother {
    public static final String GUIDO_PARTY = "GUIDO";
    public static final String FRANKY_PARTY = "FRANKY";
    public static final String CRONOS_PARTY = "CRONOS";
    public static final String SOCRATES_PARTY = "SOCRATES";
    public static final String MOTION_ID = "51_216";
    static final List<PartyVotes> ONE_GUIDO_PARTY_VOTES = List.of(
        new PartyVotes(GUIDO_PARTY, 1),
        new PartyVotes(FRANKY_PARTY, 0),
        new PartyVotes(CRONOS_PARTY, 0)
    );
    static final List<PartyVotes> NO_PARTY_VOTES = List.of(
        new PartyVotes(GUIDO_PARTY, 0),
        new PartyVotes(FRANKY_PARTY, 0),
        new PartyVotes(CRONOS_PARTY, 0)
    );
    static final Votes NO_NOS_VOTES = Votes.noVotes(0, NO_PARTY_VOTES);
    static final List<PartyVotes> TRIPLE_D_PARTY_VOTES = List.of(
        new PartyVotes(GUIDO_PARTY, 2),
        new PartyVotes(FRANKY_PARTY, 3),
        new PartyVotes(CRONOS_PARTY, 0)
    );
    static final Votes YES_VOTES = Votes.yesVotes(5, TRIPLE_D_PARTY_VOTES);
    static final List<PartyVotes> ONE_CRONOS_PARTY_VOTES = List.of(
        new PartyVotes(GUIDO_PARTY, 0),
        new PartyVotes(FRANKY_PARTY, 0),
        new PartyVotes(CRONOS_PARTY, 1));
    static final Votes ABS_VOTES = Votes.absVotes(1, ONE_CRONOS_PARTY_VOTES);
    static final Motion FIRST_MOTION =
        new Motion("first", "plenaryA", "2024-01-01", 1, "NL Title 1", "FR Title 1",  new DocumentReference("2459/1-1", null, List.of()),
            "De eerste", "Le premier", createVoteCount());
    static final Motion SECOND_MOTION =
        new Motion("second", "plenaryA", "2024-01-01", 1, "NL Title 2", "FR Title 2",  new DocumentReference("2459/1-2", null, List.of()),
            "De tweede", "Le deuxieme", createNoNOsVoteCount());
    static final Motion FOURTH_MOTION =
        new Motion("fourth", "plenaryB", "2024-01-03", 1, "NL Title 4", "FR Title 4",  new DocumentReference("2459/1-4", null, List.of()),
            "De vierde", "Le quatrieme", createVoteCount());
    static final List<PartyVotes> SEVEN_PARTY_VOTES = List.of(
        new PartyVotes(GUIDO_PARTY, 3),
        new PartyVotes(FRANKY_PARTY, 2),
        new PartyVotes(SOCRATES_PARTY, 1),
        new PartyVotes(CRONOS_PARTY, 1));
    static final Motion THIRD_MOTION =
        new Motion("third", "plenaryB", "2024-01-02", 1, "NL Title 3", "FR Title 3",  new DocumentReference("2459/1-3", null, List.of()), "De derde", "Le troisieme", createSevenVoteCount());

    static final List<Motion> DUMMY_MOTIONS = List.of(
        FIRST_MOTION,
        SECOND_MOTION,
        THIRD_MOTION,
        FOURTH_MOTION
    );
    static final MotionGroup DUMMY_GROUP_1 = new MotionGroup("groupId1",
        "grouptitleNL1",
        "grouptitleFR1",
        List.of(FIRST_MOTION, SECOND_MOTION), "2024-25-05"
    );
    static final MotionGroup DUMMY_GROUP_2 = new MotionGroup("groupId2",
        "grouptitleNL2",
        "grouptitleFR2",
        List.of(THIRD_MOTION, FOURTH_MOTION), "2024-25-05"
    );
    static final List<MotionGroup> DUMMY_MOTION_GROUPS = List.of(DUMMY_GROUP_1, DUMMY_GROUP_2);

    private MotionsMother() {
    }

    private static VoteCount createVoteCount() {
        return new VoteCount(MOTION_ID, YES_VOTES, Votes.noVotes(1, ONE_GUIDO_PARTY_VOTES), ABS_VOTES);
    }

    private static VoteCount createNoNOsVoteCount() {
        return new VoteCount(MOTION_ID, YES_VOTES, NO_NOS_VOTES, ABS_VOTES);
    }

    private static VoteCount createSevenVoteCount() {
        return new VoteCount(MOTION_ID, Votes.yesVotes(7, SEVEN_PARTY_VOTES), NO_NOS_VOTES, Votes.absVotes(7, SEVEN_PARTY_VOTES));
    }
}
