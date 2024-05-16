package be.tr.democracy.query;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.PartyVotes;
import be.tr.democracy.vocabulary.VoteCount;
import be.tr.democracy.vocabulary.Votes;

import java.util.List;

public class ObjectMother {
    static final List<PartyVotes> GUIDO_PARTY = List.of(new PartyVotes("GUIDO", 666));
    static final List<PartyVotes> FRANKY_PARTY = List.of(new PartyVotes("GUIDO", 50), new PartyVotes("FRANKY", 50));
    static final Votes yesVotes = Votes.yesVotes(100, FRANKY_PARTY);
    static final Votes noVotes = Votes.noVotes(666, GUIDO_PARTY);
    static final Votes absVotes = Votes.absVotes(0, List.of());
    static final Motion FIRST = new Motion("first", "plenaryA", "2024-01-01", 1, "NL Title 1", "FR Title 1", "2459/1-1", "De eerste", "Le premier", createVoteCount());
    static final Motion SECOND = new Motion("second", "plenaryA", "2024-01-01", 1, "NL Title 2", "FR Title 2", "2459/1-2", "De tweede", "Le deuxieme", createVoteCount());
    static final Motion THIRD = new Motion("third", "plenaryB", "2024-01-02", 1, "NL Title 3", "FR Title 3", "2459/1-3", "De derde", "Le troisieme", createVoteCount());
    static final Motion FOURTH = new Motion("fourth", "plenaryB", "2024-01-03", 1, "NL Title 4", "FR Title 4", "2459/1-4", "De vierde", "Le quatrieme", createVoteCount());



    private ObjectMother() {
    }

    private static VoteCount createVoteCount() {
        return new VoteCount("51_216", yesVotes, noVotes, absVotes);
    }
}