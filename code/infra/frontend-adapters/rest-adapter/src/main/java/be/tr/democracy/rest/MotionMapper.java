package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.Motion;

import java.util.List;

public class MotionMapper {

    private MotionMapper() {
    }

    public static MotionViewDTO map(Motion x) {
        final var voteCount = x.voteCount();
        return new MotionViewDTO(
                x.titleNL(), x.titleFR(),
                new VotesViewDTO(voteCount.yesVotes().nrOfVotes(), createDummyPartyVotes()),
                new VotesViewDTO(voteCount.noVotes().nrOfVotes(), createDummyPartyVotes()),
                new VotesViewDTO(voteCount.absentees().nrOfVotes(), createDummyPartyVotes()),
                x.date(),
                x.descriptionNL(),
                x.descriptionFR(),
                voteCount.votePassed());
    }

    private static List<PartyVotesViewDTO> createDummyPartyVotes() {
        return List.of(
                new PartyVotesViewDTO("Guido", 25, 42),
                new PartyVotesViewDTO("Franky", 25, 666),
                new PartyVotesViewDTO("Triple D", 50, 8)

        );
    }
}
