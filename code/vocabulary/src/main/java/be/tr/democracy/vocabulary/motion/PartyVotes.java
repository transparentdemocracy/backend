package be.tr.democracy.vocabulary.motion;

public record PartyVotes(String partyName, int numberOfVotes) {
    public PartyVotes {
        validatePartyName(partyName);
        validateNrOfVotes(numberOfVotes);
    }

    private void validatePartyName(String partyName) {
        if (partyName == null || partyName.isBlank()) {
            throw new IllegalArgumentException("Party name cannot be null or empty");
        }
    }

    private void validateNrOfVotes(int numberOfVotes) {
        if (numberOfVotes < 0) {
            throw new IllegalArgumentException("Number of votes cannot be negative");
        }
    }
}
