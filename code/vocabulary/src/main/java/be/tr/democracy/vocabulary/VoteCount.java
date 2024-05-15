package be.tr.democracy.vocabulary;

import java.util.Objects;

public record VoteCount(Votes yesVotes, Votes noVotes, Votes absentees) {
    public VoteCount {
        Objects.requireNonNull(yesVotes);
        Objects.requireNonNull(noVotes);
        Objects.requireNonNull(absentees);
        if (yesVotes.voteType() != VoteType.YES) throw new IllegalArgumentException("Vote type must be of type YES");
        if (noVotes.voteType() != VoteType.NO) throw new IllegalArgumentException("Vote type must be of type NO");
        if (absentees.voteType() != VoteType.ABSTENTION) throw new IllegalArgumentException("Vote type must be of type ABSTENTION");
    }

    public boolean votePassed() {
        return yesVotes.nrOfVotes() > noVotes.nrOfVotes();
    }
}
