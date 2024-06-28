package be.tr.democracy.query;

import be.tr.democracy.vocabulary.motion.VoteCount;
import be.tr.democracy.vocabulary.plenary.Politician;

import java.util.List;
import java.util.Map;

public interface VoteReadModel {

    Map<String, VoteCount> getVoteCountsByVotingIds(List<String> votingIds, Map<String, Politician> politiciansById);
}
