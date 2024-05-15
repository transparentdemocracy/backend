package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.PartyVotes;
import be.tr.democracy.vocabulary.VoteCount;
import be.tr.democracy.vocabulary.Votes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
//TODO guido en franky
public class VoteCountFactory {
    private final Logger logger = LoggerFactory.getLogger(VoteCountFactory
            .class);

    private static int countVotes(List<VoteDTO> motionVotes, String voteType) {
        final var count = motionVotes.stream().filter(x -> x.vote_type().equalsIgnoreCase(voteType)).count();
        return Long.valueOf(count).intValue();
    }

    public VoteCount createVoteCount(List<VoteDTO> motionVotes) {

        final var yesVotes = countVotes(motionVotes, "YES");
        final var noVotes = countVotes(motionVotes, "NO");
        final var absentVotes = countVotes(motionVotes, "ABSTENTION");
        if ((yesVotes + noVotes + absentVotes) != motionVotes.size()) {
            logger.error("The votes do not match. Total nr of votes {} yes: {} no: {} absent: {}", motionVotes.size(), yesVotes, noVotes, absentVotes);
        }
        final List<PartyVotes> yesPartVotes = List.of();
        final List<PartyVotes> noPartVotes = List.of();
        final List<PartyVotes> abstPartVotes = List.of();
        return new VoteCount(Votes.yesVotes(yesVotes, yesPartVotes), Votes.noVotes(noVotes, noPartVotes), Votes.absVotes(absentVotes, abstPartVotes));
    }
}
