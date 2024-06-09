package be.tr.democracy.rest;

import java.util.List;

public record VotesViewDTO(int nrOfVotes, double votePercentage, List<PartyVotesViewDTO> partyVotes) {
}
