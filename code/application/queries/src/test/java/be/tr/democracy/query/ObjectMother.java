package be.tr.democracy.query;

import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.PartyVotes;
import be.tr.democracy.vocabulary.motion.VoteCount;
import be.tr.democracy.vocabulary.motion.Votes;

import java.util.List;

public class ObjectMother {
    static final List<PartyVotes> GUIDO_PARTY = List.of(new PartyVotes("GUIDO", 666));
    static final List<PartyVotes> FRANKY_PARTY = List.of(new PartyVotes("GUIDO", 50), new PartyVotes("FRANKY", 50));
    static final Votes yesVotes = Votes.yesVotes(100, FRANKY_PARTY);
    static final Votes noVotes = Votes.noVotes(666, GUIDO_PARTY);
    static final Votes absVotes = Votes.absVotes(0, List.of());
    static final Motion FIRST_MOTION =
        new Motion(
            "first",
            "NL Title 1",
            "FR Title 1",
            new DocumentReference("2459/1-1", null, List.of()),
            "De eerste",
            "2024-01-01",
            createVoteCount(),
            "vote-101",
            false,
            "plenaryId",
            1);
    static final Motion SECOND_MOTION =
        new Motion("second",
            "NL Title 2",
            "FR Title 2",
            new DocumentReference("2459/1-2", null, List.of()),
            "De tweede",
            "2024-01-01",
            createVoteCount(),
            "vote-102",
            false,
            "plenaryId",
            1);
    static final Motion THIRD_MOTION =
        new Motion("third",
            "NL Title 3",
            "FR Title 3",
            new DocumentReference("2459/1-3", null, List.of()),
            "De derde",
            "2024-01-03",
            createVoteCount(),
            "vote-101",
            false,
            "plenaryId",
            1);
    static final Motion FOURTH_MOTION =
        new Motion("fourth",
            "NL Title 4",
            "FR Title 4",
            new DocumentReference("2459/1-4", null, List.of()),
            "De vierde",
            "2024-01-03",
            createVoteCount(),
            "vote-104",
            false,
            "plenaryId",
            1);

    private ObjectMother() {
    }

    private static VoteCount createVoteCount() {
        return new VoteCount("51_216", yesVotes, noVotes, absVotes);
    }
}