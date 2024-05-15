package be.tr.democracy.rest;

import java.util.List;

public record VotesViewDTO(int nrOfVotes, List<PartyVotesViewDTO> partyVotes) {
}
