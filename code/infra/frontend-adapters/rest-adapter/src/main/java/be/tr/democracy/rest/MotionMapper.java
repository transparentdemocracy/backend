package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.PartyVotes;
import be.tr.democracy.vocabulary.Votes;

import java.util.List;

public class MotionMapper {

    private MotionMapper() {
    }

    public static MotionViewDTO map(Motion motion) {
        final var voteCount = motion.voteCount();
        return new MotionViewDTO(
                motion.titleNL(),
                motion.titleFR(),
                mapVotes(voteCount.yesVotes()),
                mapVotes(voteCount.noVotes()),
                mapVotes(voteCount.abstention()),
                motion.date(),
                motion.descriptionNL(),
                motion.descriptionFR(),
                voteCount.votePassed());
    }

    private static VotesViewDTO mapVotes(Votes yesVotes) {
        return new VotesViewDTO(yesVotes.nrOfVotes(), mapVoteCount(yesVotes));
    }

    private static List<PartyVotesViewDTO> mapVoteCount(Votes votes) {
        final int total = votes.nrOfVotes();
        return votes.partyVotes()
                .stream()
                .map(x -> buildPartyVotes(x, total))
                .toList();
    }

    private static PartyVotesViewDTO buildPartyVotes(PartyVotes x, int total) {
        final int numberOfVotes = x.numberOfVotes();
        final int i = calculatePercentage(total, (double) numberOfVotes);
        return new PartyVotesViewDTO(x.partyName(), i, numberOfVotes);
    }

    private static int calculatePercentage(int total, double numberOfVotes) {
        if (total == 0) return 0;
        else {
            final double percentage = (numberOfVotes / total) * 100;

            final var round = Math.round(percentage);
            return Long.valueOf(round).intValue();
        }
    }


}
