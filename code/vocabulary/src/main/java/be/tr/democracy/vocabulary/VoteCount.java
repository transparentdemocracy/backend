package be.tr.democracy.vocabulary;

import java.util.Objects;

public record VoteCount(String motionId, Votes yesVotes, Votes noVotes, Votes abstention) {
    public VoteCount {
        Objects.requireNonNull(yesVotes);
        Objects.requireNonNull(noVotes);
        Objects.requireNonNull(abstention);
        if (yesVotes.voteType() != VoteType.YES) throw new IllegalArgumentException("Vote type must be of type YES");
        if (noVotes.voteType() != VoteType.NO) throw new IllegalArgumentException("Vote type must be of type NO");
        if (abstention.voteType() != VoteType.ABSTENTION)
            throw new IllegalArgumentException("Vote type must be of type ABSTENTION");
    }

    public boolean votePassed() {
        return yesVotes.nrOfVotes() > noVotes.nrOfVotes();
    }

    public int totalVotes() {
        return yesVotes.nrOfVotes() + noVotes.nrOfVotes() + abstention.nrOfVotes();
    }
}
