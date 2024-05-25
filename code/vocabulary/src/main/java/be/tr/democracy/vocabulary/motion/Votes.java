package be.tr.democracy.vocabulary.motion;

import java.util.List;
import java.util.Objects;

public record Votes(VoteType voteType, int nrOfVotes, List<PartyVotes> partyVotes) {

    public Votes {
        Objects.requireNonNull(partyVotes);
        if (nrOfVotes < 0) throw new IllegalArgumentException("Nr of votes must be positive but was " + nrOfVotes);
        final int sumOfPartyVotes = partyVotes.stream().mapToInt(PartyVotes::numberOfVotes).sum();
        if (nrOfVotes != sumOfPartyVotes) throw new IllegalArgumentException("Sum of party votes does not match the total number of votes: Total votes: " + nrOfVotes + " Total of Party votes: " + sumOfPartyVotes);
    }

    public static Votes yesVotes(int nrOfVotes, List<PartyVotes> partyVotes) {
        return new Votes(VoteType.YES, nrOfVotes, partyVotes);
    }

    public static Votes noVotes(int nrOfVotes, List<PartyVotes> partyVotes) {
        return new Votes(VoteType.NO, nrOfVotes, partyVotes);
    }

    public static Votes absVotes(int nrOfVotes, List<PartyVotes> partyVotes) {
        return new Votes(VoteType.ABSTENTION, nrOfVotes, partyVotes);
    }
}
