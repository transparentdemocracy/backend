package be.tr.democracy.query;

import be.tr.democracy.api.UpsertVote;
import be.tr.democracy.vocabulary.vote.Vote;

public class UpsertVoteCommand implements UpsertVote {

    private final VoteWriteModel voteWriteModel;

    public UpsertVoteCommand(VoteWriteModel voteWriteModel) {
        this.voteWriteModel = voteWriteModel;
    }

    @Override
    public void upsert(Vote vote) {
        voteWriteModel.upsert(vote);
    }
}
