package be.tr.democracy.vocabulary;

public record VoteCount(int nrOfYesVotes, int nrOfNoVotes, int nrOfAbsentees) {
    public VoteCount {
        validateVotes(nrOfYesVotes);
        validateVotes(nrOfNoVotes);
        validateVotes(nrOfAbsentees);
    }



    public boolean votePassed() {
        return nrOfYesVotes > nrOfNoVotes;
    }

    private static void validateVotes(int nrOfVotes) {
        if (nrOfVotes < 0) throw new RuntimeException("Nr of votes must be positive but was " + nrOfVotes);
    }
}
