package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.PartyVotes;
import be.tr.democracy.vocabulary.motion.VoteCount;
import be.tr.democracy.vocabulary.motion.VoteType;
import be.tr.democracy.vocabulary.motion.Votes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoteCountFactory {
    public static final String ABSTENTION_TOKEN = "ABSTENTION";
    public static final String NO_TOKEN = "NO";
    public static final String YES_TOKEN = "YES";
    private final Logger logger = LoggerFactory.getLogger(VoteCountFactory
            .class);
    private final Map<String, PoliticianDTO> politicianDTOS;

    public VoteCountFactory(Map<String, PoliticianDTO> politicianDTO) {
        Objects.requireNonNull(politicianDTO);
        this.politicianDTOS = politicianDTO;
    }

    private Votes countVotes(List<VoteDTO> motionVotes, String voteType) {
        final var partyVoteMap = buildPartyVoteCountMap();
        final var voteList = motionVotes.stream()
                .filter(x -> x.vote_type().equalsIgnoreCase(voteType))
                .toList();
        final var mappedVoteType = mapVoteType(voteType);

        for (var vote : voteList) {
            if(politicianDTOS.containsKey(vote.politician_id())) {

            var partyName = politicianDTOS.get(vote.politician_id()).party();
            partyVoteMap.compute(partyName, (k, currentVoteCount) -> currentVoteCount + 1);
            }
            else {
                logger.error("Could not find Politican with id {} from vote {}", vote.politician_id(),vote);
            }
        }

        final List<PartyVotes> partyVotes = partyVoteMap.entrySet().stream().map(x -> new PartyVotes(x.getKey(), x.getValue())).toList();

        return new Votes(mappedVoteType, voteList.size(), partyVotes);
    }

    private VoteType mapVoteType(String voteType) {
        switch (voteType) {
            case YES_TOKEN: return VoteType.YES;
            case NO_TOKEN: return VoteType.NO;
            case ABSTENTION_TOKEN: return VoteType.ABSTENTION;
            default: logger.error("Unknown vote type: {}", voteType);
        }
        return VoteType.ABSTENTION;
    }

    private Map<String, Integer> buildPartyVoteCountMap() {
        return politicianDTOS
                .values()
                .stream()
                .map(PoliticianDTO::party)
                .distinct()
                .collect(Collectors.toMap(x -> x, x -> 0));
    }

    public Optional<VoteCount> createVoteCount(List<VoteDTO> motionVotes) {
        if (motionVotes.isEmpty()) {
            logger.error("No motion votes received!");
            return Optional.empty();
        }
        final long uniqueMotionIdCount = motionVotes.stream().map(VoteDTO::voting_id).distinct().count();
        if (uniqueMotionIdCount != 1) {
            logger.error("More than one motion id received: {}", uniqueMotionIdCount);
            return Optional.empty();
        }
        final var yesVotes = countVotes(motionVotes, YES_TOKEN);
        final var noVotes = countVotes(motionVotes, NO_TOKEN);
        final var absentVotes = countVotes(motionVotes, ABSTENTION_TOKEN);
        if ((yesVotes.nrOfVotes() + noVotes.nrOfVotes() + absentVotes.nrOfVotes()) != motionVotes.size()) {
            logger.error("The votes do not match. Total nr of votes {} yes: {} no: {} absent: {}", motionVotes.size(), yesVotes, noVotes, absentVotes);
        }
        return Optional.of(new VoteCount(motionVotes.getFirst().voting_id(), yesVotes, noVotes, absentVotes));
    }
}
