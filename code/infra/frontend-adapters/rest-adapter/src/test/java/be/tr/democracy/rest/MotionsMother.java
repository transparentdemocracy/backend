package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.PartyVotes;
import be.tr.democracy.vocabulary.VoteCount;
import be.tr.democracy.vocabulary.Votes;

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
    static final List<PartyVotes> SEVEN_PARTY_VOTES = List.of(
            new PartyVotes(GUIDO_PARTY, 3),
            new PartyVotes(FRANKY_PARTY, 2),
            new PartyVotes(SOCRATES_PARTY, 1),
            new PartyVotes(CRONOS_PARTY, 1));
    static final Votes ABS_VOTES = Votes.absVotes(1, ONE_CRONOS_PARTY_VOTES);
    static final Motion FIRST_MOTION = new Motion("first", "plenaryA", "2024-01-01", 1, "NL Title 1", "FR Title 1", "2459/1-1", "De eerste", "Le premier", createVoteCount());
    static final Motion SECOND_MOTION = new Motion("second", "plenaryA", "2024-01-01", 1, "NL Title 2", "FR Title 2", "2459/1-2", "De tweede", "Le deuxieme", createNoNOsVoteCount());
    static final Motion THIRD_MOTION = new Motion("third", "plenaryB", "2024-01-02", 1, "NL Title 3", "FR Title 3", "2459/1-3", "De derde", "Le troisieme", createSevenVoteCount());
    static final List<Motion> THREE_MOTIONS = List.of(
            FIRST_MOTION,
            SECOND_MOTION,
            THIRD_MOTION
    );
    static final Motion FOURTH = new Motion("fourth", "plenaryB", "2024-01-03", 1, "NL Title 4", "FR Title 4", "2459/1-4", "De vierde", "Le quatrieme", createVoteCount());
    static final List<Motion> DUMMY_MOTIONS = List.of(
            FIRST_MOTION,
            SECOND_MOTION,
            THIRD_MOTION,
            FOURTH
    );

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
